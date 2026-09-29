import { Component, output } from '@angular/core';
import { TranslationService } from '../../../core/services/translation.service';
import { ThemeService } from '../../../core/services/theme.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  menuToggle = output<void>();

  constructor(
    public i18n: TranslationService,
    public theme: ThemeService,
    public auth: AuthService
  ) {}
}
