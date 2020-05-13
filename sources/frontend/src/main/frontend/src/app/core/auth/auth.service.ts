import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TokenStorage } from '../token/token.storage';
import { User } from '../../../model/user.model';
import { UserStorage } from '../userstorage/user.storage';
import { UrlConfig } from '../config/url-config';

@Injectable()
export class AuthService {

  private currentUser: User;

  constructor(private http: HttpClient, private tokenStorage: TokenStorage, private userStorage: UserStorage) {
  }

  attemptAuth(mail: string, password: string): Observable<any> {
    const credentials = {mail: mail, password: password};
    console.log('attempAuth ::');
    return this.http.post<any>(UrlConfig.API_URL + '/api/auth/sign-in', credentials);
  }

  isAuthenticated(): boolean {
    const token = this.tokenStorage.getToken();

    if (token !== null) {
      return true;
    }

    return false;
  }

  getCurrentUser() {
    return this.currentUser;
  }

  setCurrentUser(user: User) {
    this.currentUser = user;
  }

  logout() {
    this.currentUser = null;
    this.tokenStorage.signOut();
    this.userStorage.deleteUser();
  }

}
