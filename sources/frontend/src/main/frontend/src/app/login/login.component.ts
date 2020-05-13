import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../../model/user.model';
// import { LoginService } from '../service/login.service';
import { AuthService } from '../core/auth/auth.service';
import { TokenStorage } from '../core/token/token.storage';
import { UserStorage } from '../core/userstorage/user.storage';
import { FormGroup, FormControl, FormBuilder, Validators, PatternValidator, ValidatorFn } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  errorMessage: string;
  

  form: FormGroup;

  constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router, private token: TokenStorage, private userStorage: UserStorage) { }
  // constructor(private router: Router) { }

  ngOnInit() {
    this.errorMessage = "";
    this.form = this.formBuilder.group({
      loginmail : ['', [Validators.required, Validators.pattern("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")]],
      loginpassword: ['', Validators.required]
    });
  }




  login({value, valid}: {value: any, valid: boolean}) {
    this.errorMessage = "";

    this.authService.attemptAuth(value.loginmail, value.loginpassword).subscribe(
      data => {
        if (data.status === "OK") {
          let credentials = JSON.parse(data.message);
  
          this.token.saveToken(credentials.token);
          this.router.navigate(['acceuil']);
          const newUser: User = new User(credentials.user.prenom, credentials.user.nom, credentials.user.mail);
          this.authService.setCurrentUser(newUser);
          this.userStorage.saveUser(JSON.stringify(newUser));
        } else {
          this.errorMessage = data.message;
        }

      }, error => {
        this.errorMessage = error.message;
      }
    );
  }

}
