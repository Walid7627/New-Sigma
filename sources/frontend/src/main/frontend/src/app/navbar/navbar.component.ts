import { Component, OnInit } from '@angular/core';
import { AuthService } from '../core/auth/auth.service';
import { Router } from '@angular/router';
import { User } from '../../model/user.model';
import { UserStorage } from '../core/userstorage/user.storage';
import { RoleService } from '../core/role/role.service';


@Component({
  selector: 'app-nav-bar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})

export class NavbarComponent implements OnInit {
  constructor (private authService: AuthService, private router: Router,
    private userStorage: UserStorage, private roleService: RoleService) { }

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['acceuil']);
  }

  ngOnInit() {
    if (this.authService.isAuthenticated()) {
      const user: User = JSON.parse(this.userStorage.getUser());
      this.authService.setCurrentUser(user);
    }
  }

  canActivateRoute(requiredRole : String) {
    return this.roleService.canActivate(requiredRole);
  }

  canViewProfile() {
    return (this.isAuthenticated() && this.roleService.canActivate("ROLE_FOURNISSEUR"));
  }

  canListProviders() {
     return this.isAuthenticated() && !this.roleService.canActivate("ROLE_FOURNISSEUR");
  }
  

  canListEntities() {
    return this.isAuthenticated() && this.roleService.canActivate("ROLE_ADMINISTRATEUR_SIGMA");
  }

  canListAdminsEntity() {
    return this.isAuthenticated() && this.roleService.canActivate("ROLE_ADMINISTRATEUR_SIGMA");
  }

  canListTeams() {
    return this.isAuthenticated() && this.roleService.canActivate("ROLE_ADMINISTRATEUR_ENTITE");
  }
  canListVisiteurs() {
    return this.isAuthenticated() && (this.roleService.canActivate("ROLE_ADMINISTRATEUR_ENTITE")|| 
    this.roleService.canActivate("ROLE_ADMINISTRATEUR_SIGMA"));
 }

  canListPurchasers() {
    return this.isAuthenticated() && (this.roleService.canActivate("ROLE_ADMINISTRATEUR_ENTITE") || 
      this.roleService.canActivate("ROLE_RESPONSABLE_ACHAT") );
  }

  canListSegments() {
    return this.isAuthenticated() && (this.roleService.canActivate("ROLE_ADMINISTRATEUR_ENTITE") || 
    this.roleService.canActivate("ROLE_RESPONSABLE_ACHAT"));
  }
}
