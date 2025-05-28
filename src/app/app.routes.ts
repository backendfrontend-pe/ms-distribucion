import { Routes } from '@angular/router';
import { AuthGuard } from './layouts/auth/auth.component';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./views/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'admin',
    canActivate: [AuthGuard],
    canActivateChild: [AuthGuard],
    loadComponent: () =>
      import('./layouts/admin/admin.component').then(m => m.AdminComponent),
    children: [
      {
        path: 'zones',
        loadChildren: () =>
          import('./views/admin/zones/zones.routes').then(m => m.ZONE_ROUTES)
      },
      {
        path: 'streets',
        loadChildren: () =>
          import('./views/admin/streets/streets.routes').then(m => m.STREET_ROUTES)
      },
      { path: '', redirectTo: 'zones', pathMatch: 'full' }
    ]
  },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];
