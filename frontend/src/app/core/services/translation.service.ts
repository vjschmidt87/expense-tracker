import { Injectable, signal, computed } from '@angular/core';
import { en } from '../i18n/en';
import { ptBr } from '../i18n/pt-br';

export type Lang = 'en' | 'pt-br';

@Injectable({ providedIn: 'root' })
export class TranslationService {
  private translations: Record<Lang, Record<string, string>> = { en, 'pt-br': ptBr };
  lang = signal<Lang>((localStorage.getItem('lang') as Lang) || 'en');
  currentTranslations = computed(() => this.translations[this.lang()]);

  t(key: string, params?: Record<string, string>): string {
    let value = this.currentTranslations()[key] || key;
    if (params) {
      Object.entries(params).forEach(([k, v]) => {
        value = value.replace(`{{${k}}}`, v);
      });
    }
    return value;
  }

  setLang(lang: Lang): void {
    this.lang.set(lang);
    localStorage.setItem('lang', lang);
  }

  toggleLang(): void {
    this.setLang(this.lang() === 'en' ? 'pt-br' : 'en');
  }
}
