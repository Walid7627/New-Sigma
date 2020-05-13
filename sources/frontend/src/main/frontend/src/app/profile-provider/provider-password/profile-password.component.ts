import {Component, OnInit, ViewChild} from "@angular/core";
import {AuthService} from "../../core/auth/auth.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../service/user.service";
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';



@Component({
  selector: 'app-provider-password',
  templateUrl: './profile-password.component.html',
  styleUrls: ['./profile-password.component.css']
})
export class ProfilePasswordComponent implements OnInit{

  form: FormGroup;

  newPassword: string;
  confirmNewPassword: string;

  password = {
    newPassword: "",
    confirmNewPassword: ""
  }

  @ViewChild(ToastContainerDirective, { static: true }) toastContainer: ToastContainerDirective;
  constructor(private authService: AuthService, private fb: FormBuilder, private userService: UserService, private toastrService: ToastrService){
  }

  ngOnInit() {
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
      this.userService.modifyPassword(this.authService.getCurrentUser(), this.password.newPassword).subscribe(
        res => {
          this.showToastSuccessMessage("Votre mot de passe a été bien modifié", "Changement de mot de passe");
	this.form.reset();
        }, err => {
          this.showToastErrorMessage("Erreur lors de la modification du mot de passe, Réessayez !","Changement de mot de passe" );
        }
      );
      
    }
  }

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
