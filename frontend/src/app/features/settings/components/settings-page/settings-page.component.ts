import { Component } from '@angular/core';
import { TranslationService } from '../../../../core/services/translation.service';
import { ThemeService } from '../../../../core/services/theme.service';

@Component({
  selector: 'app-settings-page',
  standalone: true,
  templateUrl: './settings-page.component.html',
  styleUrl: './settings-page.component.scss'
})
export class SettingsPageComponent {
  constructor(public i18n: TranslationService, public theme: ThemeService) {}
}
