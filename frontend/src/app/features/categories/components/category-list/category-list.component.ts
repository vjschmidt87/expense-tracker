import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../../core/services/api.service';
import { TranslationService } from '../../../../core/services/translation.service';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-category-list',
  standalone: true,
  imports: [FormsModule, ConfirmDialogComponent],
  templateUrl: './category-list.component.html',
  styleUrl: './category-list.component.scss'
})
export class CategoryListComponent implements OnInit {
  categories = signal<any[]>([]);
  showForm = signal(false);
  editId = signal<number | null>(null);
  deleteTarget = signal<number | null>(null);

  form = { name: '', namePt: '', icon: '', type: 'EXPENSE' };

  constructor(private api: ApiService, public i18n: TranslationService) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.api.getCategories().subscribe(c => this.categories.set(c));
  }

  getName(c: any): string {
    return this.i18n.lang() === 'pt-br' ? c.namePt : c.name;
  }

  isDefault(c: any): boolean {
    return !c.userId;
  }

  openAdd(): void {
    this.form = { name: '', namePt: '', icon: '', type: 'EXPENSE' };
    this.editId.set(null);
    this.showForm.set(true);
  }

  openEdit(c: any): void {
    this.form = { name: c.name, namePt: c.namePt, icon: c.icon, type: c.type };
    this.editId.set(c.id);
    this.showForm.set(true);
  }

  save(): void {
    const obs = this.editId()
      ? this.api.updateCategory(this.editId()!, this.form)
      : this.api.createCategory(this.form);
    obs.subscribe(() => {
      this.showForm.set(false);
      this.loadCategories();
    });
  }

  confirmDelete(id: number): void {
    this.deleteTarget.set(id);
  }

  doDelete(): void {
    const id = this.deleteTarget();
    if (id) {
      this.api.deleteCategory(id).subscribe(() => {
        this.deleteTarget.set(null);
        this.loadCategories();
      });
    }
  }
}
