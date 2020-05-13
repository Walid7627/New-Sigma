import {Component, OnInit, ViewChild} from "@angular/core";
import {AuthService} from "../core/auth/auth.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../service/user.service";
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { ActivatedRoute ,Router} from "@angular/router";
@Component({
  selector: 'app-new-password',
  templateUrl: './new-password.component.html',
  styleUrls: ['./new-password.component.css']
})
export class NewPasswordComponent implements OnInit {

  form: FormGroup;
  mail = "";
  newPassword: string;
  confirmNewPassword: string;

  password = {
    newPassword: "",
    confirmNewPassword: ""
  }

  @ViewChild(ToastContainerDirective, { static: true }) toastContainer: ToastContainerDirective;
  constructor(private router: Router,private route : ActivatedRoute,private authService: AuthService, private fb: FormBuilder, private userService: UserService, private toastrService: ToastrService){
  }

  ngOnInit() {
  this.mail = this.route.snapshot.paramMap.get("mail");
    this.toastrService.overlayContainer = this.toastContainer;
    this.form = this.fb.group({
      'newPassword': ['', [Validators.compose([Validators.required]),Validators.minLength(8)]],
      'confirmNewPassword': ['', Validators.compose([Validators.required])]
    });
  }

  onSubmit() {
    if (this.password.newPassword != this.password.confirmNewPassword) {
      this.showToastErrorMessage("Les mots de passes ne sont pas conformes","Changement de mot de passe" );
    } else {
      this.userService.modifyPasswordWithMail(this.mail, this.password.newPassword).subscribe(
        res => {
         this.showToastSuccessMessage("Votre mot de passe a été bien modifié", "Changement de mot de passe"); 
          setTimeout(this.navigate.bind(this), 3000);

	this.form.reset();
        }, err => {
          this.showToastErrorMessage("Erreur lors de la modification du mot de passe, Réessayez !","Changement de mot de passe" );
        }
      );
      
    }
  }
  navigate(){ this.router.navigate(['/connexion'])}
  showToastErrorMessage(message, title) {
    this.toastrService.error(message, title, {
      timeOut: 3000,
    });
    window.scroll(0,0);
  }

  showToastSuccessMessage(message, title) {
    this.toastrService.success(message, title, {
      timeOut: 3000,
    });
    window.scroll(0,0);
  }

}

