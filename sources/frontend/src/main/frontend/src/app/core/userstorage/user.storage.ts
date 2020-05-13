import { Injectable } from '@angular/core';

const USER_KEY = 'UserInfo';

@Injectable()
export class UserStorage {

  constructor() { }

  deleteUser() {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.clear();
  }

  public saveUser(token: string) {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY,  token);
  }

  public getUser(): string {
    return sessionStorage.getItem(USER_KEY);
  }
}
