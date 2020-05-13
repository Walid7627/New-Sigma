import { Injectable } from '@angular/core';
import { TokenStorage } from '../token/token.storage';
import decode from 'jwt-decode';

@Injectable()

export class RoleService {

  constructor(public tokenStorage: TokenStorage) {}

  getRole() : String {
    let token = this.tokenStorage.getToken();

    if (token !== undefined) {
      return decode(token).role;
    } else {
      return null;
    }
  }

  canActivate(requiredRole : String) : boolean {
    return this.getRole() === requiredRole;
  }
}
