import { Component, input, output } from '@angular/core';
import { TranslationService } from '../../../core/services/translation.service';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  templateUrl: './confirm-dialog.component.html',
  styleUrl: './confirm-dialog.component.scss'
})
export class ConfirmDialogComponent {
  message = input('');
  confirmed = output<void>();
  cancelled = output<void>();

  constructor(public i18n: TranslationService) {}
}
