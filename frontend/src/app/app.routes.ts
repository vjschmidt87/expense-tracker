import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./features/auth/components/login/login.component').then(m => m.LoginComponent) },
  { path: 'register', loadComponent: () => import('./features/auth/components/register/register.component').then(m => m.RegisterComponent) },
  { path: 'dashboard', loadComponent: () => import('./features/dashboard/components/dashboard-page/dashboard-page.component').then(m => m.DashboardPageComponent), canActivate: [authGuard] },
  { path: 'transactions', loadComponent: () => import('./features/transactions/components/transaction-list/transaction-list.component').then(m => m.TransactionListComponent), canActivate: [authGuard] },
  { path: 'transactions/new', loadComponent: () => import('./features/transactions/components/transaction-form/transaction-form.component').then(m => m.TransactionFormComponent), canActivate: [authGuard] },
  { path: 'transactions/:id/edit', loadComponent: () => import('./features/transactions/components/transaction-form/transaction-form.component').then(m => m.TransactionFormComponent), canActivate: [authGuard] },
  { path: 'budgets', loadComponent: () => import('./features/budgets/components/budget-list/budget-list.component').then(m => m.BudgetListComponent), canActivate: [authGuard] },
  { path: 'categories', loadComponent: () => import('./features/categories/components/category-list/category-list.component').then(m => m.CategoryListComponent), canActivate: [authGuard] },
  { path: 'settings', loadComponent: () => import('./features/settings/components/settings-page/settings-page.component').then(m => m.SettingsPageComponent), canActivate: [authGuard] },
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: '**', redirectTo: '/dashboard' }
];
