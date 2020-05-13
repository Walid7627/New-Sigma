import { Routes } from '@angular/router';
import { ProfileProviderComponent } from './profile-provider.component';
import { RoleGuard } from '../core/role/role.guard';

export const profileProviderRoute: Routes = [
  {
    path: 'profil',
    component: ProfileProviderComponent,
    // canActivate: [RoleGuard],
    // data: {
    //   expectedRole: 'ROLE_FOURNISSEUR'
    // }
  }
];
