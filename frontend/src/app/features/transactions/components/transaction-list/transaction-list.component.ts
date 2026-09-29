import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../../../core/services/api.service';
import { TranslationService } from '../../../../core/services/translation.service';
import { CurrencyFormatPipe } from '../../../../shared/pipes/currency-format.pipe';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-transaction-list',
  standalone: true,
  imports: [FormsModule, CurrencyFormatPipe, ConfirmDialogComponent],
  templateUrl: './transaction-list.component.html',
  styleUrl: './transaction-list.component.scss'
})
export class TransactionListComponent implements OnInit {
  transactions = signal<any[]>([]);
  categories = signal<any[]>([]);
  filterType = '';
  filterCategory = '';
  filterMonth = new Date().getMonth() + 1;
  filterYear = new Date().getFullYear();
  deleteTarget = signal<number | null>(null);

  constructor(
    private api: ApiService,
    private router: Router,
    public i18n: TranslationService
  ) {}

  ngOnInit(): void {
    this.loadCategories();
    this.loadTransactions();
  }

  loadCategories(): void {
    this.api.getCategories().subscribe(c => this.categories.set(c));
  }

  loadTransactions(): void {
    const filters: Record<string, any> = { month: this.filterMonth, year: this.filterYear };
    if (this.filterType) filters['type'] = this.filterType;
    if (this.filterCategory) filters['categoryId'] = this.filterCategory;
    this.api.getTransactions(filters).subscribe(t => this.transactions.set(t));
  }

  addNew(): void {
    this.router.navigate(['/transactions/new']);
  }

  edit(id: number): void {
    this.router.navigate(['/transactions', id, 'edit']);
  }

  confirmDelete(id: number): void {
    this.deleteTarget.set(id);
  }

  doDelete(): void {
    const id = this.deleteTarget();
    if (id) {
      this.api.deleteTransaction(id).subscribe(() => {
        this.deleteTarget.set(null);
        this.loadTransactions();
      });
    }
  }

  exportCsv(): void {
    this.api.exportCsv().subscribe(csv => {
      const blob = new Blob([csv], { type: 'text/csv' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'transactions.csv';
      a.click();
      URL.revokeObjectURL(url);
    });
  }

  getDescription(tx: any): string {
    return this.i18n.lang() === 'pt-br' ? (tx.descriptionPt || tx.description) : tx.description;
  }

  getCategoryName(tx: any): string {
    return this.i18n.lang() === 'pt-br' ? tx.categoryNamePt : tx.categoryName;
  }

  getCatName(c: any): string {
    return this.i18n.lang() === 'pt-br' ? c.namePt : c.name;
  }
}
