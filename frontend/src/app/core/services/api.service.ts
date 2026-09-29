import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private url = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getCategories(): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/categories`);
  }

  createCategory(data: any): Observable<any> {
    return this.http.post<any>(`${this.url}/categories`, data);
  }

  updateCategory(id: number, data: any): Observable<any> {
    return this.http.put<any>(`${this.url}/categories/${id}`, data);
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/categories/${id}`);
  }

  getTransactions(filters?: Record<string, any>): Observable<any[]> {
    let params = new HttpParams();
    if (filters) {
      Object.entries(filters).forEach(([key, val]) => {
        if (val !== null && val !== undefined && val !== '') {
          params = params.set(key, String(val));
        }
      });
    }
    return this.http.get<any[]>(`${this.url}/transactions`, { params });
  }

  getTransaction(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/transactions/${id}`);
  }

  createTransaction(data: any): Observable<any> {
    return this.http.post<any>(`${this.url}/transactions`, data);
  }

  updateTransaction(id: number, data: any): Observable<any> {
    return this.http.put<any>(`${this.url}/transactions/${id}`, data);
  }

  deleteTransaction(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/transactions/${id}`);
  }

  exportCsv(): Observable<string> {
    return this.http.get(`${this.url}/transactions/export/csv`, { responseType: 'text' });
  }

  getBudgets(month: number, year: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/budgets`, {
      params: { month: String(month), year: String(year) }
    });
  }

  createBudget(data: any): Observable<any> {
    return this.http.post<any>(`${this.url}/budgets`, data);
  }

  updateBudget(id: number, data: any): Observable<any> {
    return this.http.put<any>(`${this.url}/budgets/${id}`, data);
  }

  deleteBudget(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/budgets/${id}`);
  }

  getDashboard(month: number, year: number): Observable<any> {
    return this.http.get<any>(`${this.url}/dashboard/summary`, {
      params: { month: String(month), year: String(year) }
    });
  }
}
