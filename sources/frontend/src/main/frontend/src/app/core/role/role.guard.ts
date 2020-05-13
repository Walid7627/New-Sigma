import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { TokenStorage } from '../token/token.storage';
import decode from 'jwt-decode';

@Injectable()
export class RoleGuard implements CanActivate {

  constructor(public auth: AuthService, public router: Router, public tokenStorage: TokenStorage) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {


    if (!this.auth.isAuthenticated()) {
      this.router.navigate(['acceuil']);
      return false;
    }

    // this will be passed from the route config
    // on the data property
    const expectedRole = route.data.expectedRole;

    const token = this.tokenStorage.getToken();

    // decode the token to get its payload
    const tokenPayload = decode(token);

    console.log(tokenPayload);

    if (tokenPayload.role !== expectedRole) {
      this.router.navigate(['acceuil']);
      return false;
    }
    return true;
  }

}
