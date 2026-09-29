import { Component, input } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { TranslationService } from '../../../core/services/translation.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  isOpen = input(false);

  navItems = [
    { route: '/dashboard', icon: '📊', label: 'nav.dashboard' },
    { route: '/transactions', icon: '💳', label: 'nav.transactions' },
    { route: '/budgets', icon: '🎯', label: 'nav.budgets' },
    { route: '/categories', icon: '🏷️', label: 'nav.categories' },
    { route: '/settings', icon: '⚙️', label: 'nav.settings' }
  ];

  constructor(public i18n: TranslationService) {}
}
