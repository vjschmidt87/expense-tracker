import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../../../../core/services/api.service';
import { TranslationService } from '../../../../core/services/translation.service';

@Component({
  selector: 'app-transaction-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './transaction-form.component.html',
  styleUrl: './transaction-form.component.scss'
})
export class TransactionFormComponent implements OnInit {
  isEdit = false;
  txId: number | null = null;
  categories = signal<any[]>([]);

  form = {
    description: '',
    descriptionPt: '',
    amount: 0,
    type: 'EXPENSE',
    categoryId: 0,
    date: new Date().toISOString().split('T')[0],
    recurring: false,
    recurrenceType: ''
  };

  constructor(
    private api: ApiService,
    private route: ActivatedRoute,
    private router: Router,
    public i18n: TranslationService
  ) {}

  ngOnInit(): void {
    this.api.getCategories().subscribe(c => this.categories.set(c));
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.txId = +id;
      this.api.getTransaction(this.txId).subscribe(tx => {
        this.form = {
          description: tx.description,
          descriptionPt: tx.descriptionPt || '',
          amount: tx.amount,
          type: tx.type,
          categoryId: tx.categoryId,
          date: tx.date,
          recurring: tx.recurring,
          recurrenceType: tx.recurrenceType || ''
        };
      });
    }
  }

  filteredCategories(): any[] {
    return this.categories().filter(c => c.type === this.form.type);
  }

  getCatName(c: any): string {
    return this.i18n.lang() === 'pt-br' ? c.namePt : c.name;
  }

  onSubmit(): void {
    const data = { ...this.form };
    if (!data.recurring) {
      data.recurrenceType = '';
    }
    const obs = this.isEdit
      ? this.api.updateTransaction(this.txId!, data)
      : this.api.createTransaction(data);
    obs.subscribe(() => this.router.navigate(['/transactions']));
  }

  cancel(): void {
    this.router.navigate(['/transactions']);
  }
}
