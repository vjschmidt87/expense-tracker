import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'currencyFormat', standalone: true })
export class CurrencyFormatPipe implements PipeTransform {
  transform(value: number | null | undefined, lang: string = 'en'): string {
    if (value == null) return '-';
    const locale = lang === 'pt-br' ? 'pt-BR' : 'en-US';
    const currency = lang === 'pt-br' ? 'BRL' : 'USD';
    return new Intl.NumberFormat(locale, { style: 'currency', currency }).format(value);
  }
}
