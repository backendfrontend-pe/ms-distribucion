import { Routes } from '@angular/router';
import { StreetListComponent } from './street-list/street-list.component';

export const STREET_ROUTES: Routes = [
  { path: '', component: StreetListComponent },
  {
    path: 'crear',
    loadComponent: () =>
      import('./street-form/street-form.component').then(m => m.StreetFormComponent)
  },
  {
    path: 'editar/:id',
    loadComponent: () =>
      import('./street-form/street-form.component').then(m => m.StreetFormComponent)
  }
];
