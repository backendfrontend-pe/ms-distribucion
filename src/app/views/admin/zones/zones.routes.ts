import { Routes } from '@angular/router';
import { ZoneListComponent } from './zone-list/zone-list.component';

export const ZONE_ROUTES: Routes = [
  { path: '', component: ZoneListComponent },
  
  

  {
    path: 'crear',
    loadComponent: () =>
      import('./zone-form/zone-form.component').then(m => m.ZoneFormComponent)
  },
  {
    path: 'editar/:id',
    loadComponent: () =>
      import('./zone-form/zone-form.component').then(m => m.ZoneFormComponent)
  }
];
