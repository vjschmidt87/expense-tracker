import { Component, OnInit, signal, computed } from '@angular/core';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData } from 'chart.js';
import { Chart, registerables } from 'chart.js';
import { ApiService } from '../../../../core/services/api.service';
import { TranslationService } from '../../../../core/services/translation.service';
import { CurrencyFormatPipe } from '../../../../shared/pipes/currency-format.pipe';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [BaseChartDirective, CurrencyFormatPipe],
  templateUrl: './dashboard-page.component.html',
  styleUrl: './dashboard-page.component.scss'
})
export class DashboardPageComponent implements OnInit {
  now = new Date();
  month = signal(this.now.getMonth() + 1);
  year = signal(this.now.getFullYear());
  dashboard = signal<any>(null);
  recentTransactions = signal<any[]>([]);

  totalIncome = computed(() => this.dashboard()?.totalIncome ?? 0);
  totalExpenses = computed(() => this.dashboard()?.totalExpenses ?? 0);
  balance = computed(() => this.totalIncome() - this.totalExpenses());

  doughnutData = signal<ChartData<'doughnut'>>({ labels: [], datasets: [] });
  doughnutOptions: ChartConfiguration<'doughnut'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: { legend: { position: 'bottom', labels: { color: '#94a3b8' } } }
  };

  budgets = signal<any[]>([]);

  constructor(public api: ApiService, public i18n: TranslationService) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.api.getDashboard(this.month(), this.year()).subscribe(data => {
      this.dashboard.set(data);
      this.recentTransactions.set((data.recentTransactions ?? []).slice(0, 5));
      this.buildChart(data);
    });
    this.api.getBudgets(this.month(), this.year()).subscribe(b => this.budgets.set(b));
  }

  private buildChart(data: any): void {
    const breakdown: any[] = data.categoryBreakdown ?? [];
    const isPt = this.i18n.lang() === 'pt-br';
    const labels = breakdown.map((c: any) => isPt ? c.categoryNamePt : c.categoryName);
    const amounts = breakdown.map((c: any) => c.amount);
    const colors = ['#3b82f6', '#ef4444', '#22c55e', '#f59e0b', '#8b5cf6', '#ec4899', '#06b6d4', '#f97316'];
    this.doughnutData.set({
      labels,
      datasets: [{ data: amounts, backgroundColor: colors.slice(0, amounts.length) }]
    });
  }

  changeMonth(delta: number): void {
    let m = this.month() + delta;
    let y = this.year();
    if (m > 12) { m = 1; y++; }
    if (m < 1) { m = 12; y--; }
    this.month.set(m);
    this.year.set(y);
    this.loadData();
  }

  getCategoryName(b: any): string {
    return this.i18n.lang() === 'pt-br' ? b.categoryNamePt : b.categoryName;
  }

  getSpentPercent(b: any): number {
    return b.amount > 0 ? Math.min((b.spent / b.amount) * 100, 100) : 0;
  }

  isOverBudget(b: any): boolean {
    return b.spent > b.amount;
  }

  getDescription(tx: any): string {
    return this.i18n.lang() === 'pt-br' ? (tx.descriptionPt || tx.description) : tx.description;
  }

  monthName(): string {
    const date = new Date(this.year(), this.month() - 1);
    const locale = this.i18n.lang() === 'pt-br' ? 'pt-BR' : 'en-US';
    return date.toLocaleDateString(locale, { month: 'long', year: 'numeric' });
  }
}
