import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../../core/services/api.service';
import { TranslationService } from '../../../../core/services/translation.service';
import { CurrencyFormatPipe } from '../../../../shared/pipes/currency-format.pipe';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-budget-list',
  standalone: true,
  imports: [FormsModule, CurrencyFormatPipe, ConfirmDialogComponent],
  templateUrl: './budget-list.component.html',
  styleUrl: './budget-list.component.scss'
})
export class BudgetListComponent implements OnInit {
  budgets = signal<any[]>([]);
  categories = signal<any[]>([]);
  month = new Date().getMonth() + 1;
  year = new Date().getFullYear();
  showForm = signal(false);
  editId = signal<number | null>(null);
  deleteTarget = signal<number | null>(null);

  form = { categoryId: 0, amount: 0 };

  constructor(private api: ApiService, public i18n: TranslationService) {}

  ngOnInit(): void {
    this.loadBudgets();
    this.api.getCategories().subscribe(c =>
      this.categories.set(c.filter((cat: any) => cat.type === 'EXPENSE'))
    );
  }

  loadBudgets(): void {
    this.api.getBudgets(this.month, this.year).subscribe(b => this.budgets.set(b));
  }

  getCatName(c: any): string {
    return this.i18n.lang() === 'pt-br' ? c.namePt : c.name;
  }

  getCategoryName(b: any): string {
    return this.i18n.lang() === 'pt-br' ? b.categoryNamePt : b.categoryName;
  }

  getSpentPercent(b: any): number {
    return b.amount > 0 ? Math.min((b.spent / b.amount) * 100, 100) : 0;
  }

  isOver(b: any): boolean {
    return b.spent > b.amount;
  }

  openAdd(): void {
    this.form = { categoryId: 0, amount: 0 };
    this.editId.set(null);
    this.showForm.set(true);
  }

  openEdit(b: any): void {
    this.form = { categoryId: b.categoryId, amount: b.amount };
    this.editId.set(b.id);
    this.showForm.set(true);
  }

  save(): void {
    const data = { ...this.form, month: this.month, year: this.year };
    const obs = this.editId()
      ? this.api.updateBudget(this.editId()!, data)
      : this.api.createBudget(data);
    obs.subscribe(() => {
      this.showForm.set(false);
      this.loadBudgets();
    });
  }

  confirmDelete(id: number): void {
    this.deleteTarget.set(id);
  }

  doDelete(): void {
    const id = this.deleteTarget();
    if (id) {
      this.api.deleteBudget(id).subscribe(() => {
        this.deleteTarget.set(null);
        this.loadBudgets();
      });
    }
  }
}
