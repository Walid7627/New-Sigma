import { Component, OnInit, ViewChild } from '@angular/core';
import {
  AbstractControl,
  FormGroup,
  FormControl,
  FormBuilder,
  Validators,
  PatternValidator,
  ValidatorFn
} from '@angular/forms';
import { HttpEventType } from '@angular/common/http';
import { ProfileProviderService } from '../../profile-provider/profile-provider.service';
import { DialogService } from '../../service/dialog-service'; 
import { ToastrService, ToastContainerDirective } from 'ngx-toastr';
import { Provider } from '../../../model/provider.model';
@Component({
  selector: 'app-profile-provider-contact',
  templateUrl: './provider-contact.component.html',
  styleUrls: ['./provider-contact.component.css']
})
export class ProfileProviderContactComponent implements OnInit {

  provider : Provider = new Provider();
  registrationSuccessful: boolean = false;
  registrationError: boolean = false;
  error: string;
  contact = {
    nom:"",
    prenom:"",
    telephone:"",
    fax:"",
    mail:"",
    confirmmail:"",
    password:"",
    confirmpassword:"",
    adressePhysique:""
  }


  user: FormGroup;
  get cpwd() {
      return this.user.get('confirmpassword');
    }

  get cmail() {
      return this.user.get('confirmmail');
      }

  @ViewChild(ToastContainerDirective, { static: true }) toastContainer: ToastContainerDirective;


  constructor(private toastrService: ToastrService, private fb: FormBuilder, public profileProviderService: ProfileProviderService
,private dialogService : DialogService) {
  }

  ngOnInit() {
    this.toastrService.overlayContainer = this.toastContainer;
    this.user = this.fb.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      telephone:  ['', [Validators.required, Validators.pattern("([0-9]{2}\\s){4}[0-9]{2}"), Validators.minLength(14), Validators.maxLength(14)]],
      mobile: ['', [Validators.pattern("([0-9]{2}\\s){4}[0-9]{2}"), Validators.minLength(14), Validators.maxLength(14)]],
      fax: [''],
      mail: ['', [Validators.required, Validators.pattern("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")]],
      // confirmmail: ['', [Validators.required, mailConfirming, Validators.pattern("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")]],
      // password: ['', Validators.required],
      // confirmpassword: ['', [Validators.required, passwordConfirming]],
      adresse: ['', Validators.required],
    });

    this.loadData();
  }

  loadData() {
    this.profileProviderService.get().subscribe(
      event => {
      if (event.type === HttpEventType.Response) {
        let data:any = event.body;

        if (data.status === "OK") {
          this.provider = JSON.parse(data.message);
        }
      }
    });
    


    
  }
	

  onSubmit({ value, valid }) {
    this.error = "";
    this.registrationError = false;
    this.registrationSuccessful = false;

    this.profileProviderService.addContact(value, this.provider)
    .subscribe(response => {
      if (response.type === HttpEventType.Response) {
        let data:any = response.body;
 
        if (data.status === "OK") {
          this.loadData();
          this.registrationSuccessful = true;
          this.scrollToSuccessMessage();
	  this.showToastSuccessMessage("Contact ajouté avec succes", "Ajout de contact");
	  this.user.reset();
        } else {
          this.showToastErrorMessage("Contact non ajouté : email existe déjà", "Ajout de contact");
          console.log("Error while adding new contact");
        }
      }
    }, error => {
      this.showToastErrorMessage("Erreur lors de l'ajout de contact", "Ajout de contact");
      console.log('Error: ');
      console.log(error);
    });
  }

  removeContact(contact) {
    this.error = "";
    this.registrationError = false;
    this.registrationSuccessful = false;
this.dialogService.openConfirmDialog('Etes-vous sûr de vouloir supprimer ce contact ?')
    .afterClosed().subscribe(res =>{
      if(res){
        this.profileProviderService.removeContact(contact.id, this.provider).subscribe(
      response => {
        console.log("Remove contact response", response);
	
        if (response.type === HttpEventType.Response) {
          let data:any = response.body;
          if (data.status === "OK") {
            this.registrationSuccessful = true;
            this.loadData();
  this.showToastSuccessMessage("Contact supprimé avec succes", "Suppression de contact");
  this.scrollToSuccessMessage();
            console.log("Contact removed successfully");
          } else {
            this.showToastErrorMessage("Erreur lors de la suppression de contact", "Suppression de contact");
            this.error = data.message;
            this.registrationError = true
            this.scrollToErrorMessage();
          }
        }
      },
      err => {
        this.showToastErrorMessage("Erreur lors de la suppression de contact", "Suppression de contact");
        this.error = "Error when removing contact";
          this.registrationError = true
          this.scrollToErrorMessage();
        console.log("Error when removing contact", err);
      }
    )
        
      }
    });
   
  }

  scrollToSuccessMessage() {
    setTimeout(() => {
      document.getElementById('successmessage').scrollIntoView({
        behavior: 'smooth'
      });
    }, 350);
  }

  scrollToErrorMessage() {
    setTimeout(() => {
      document.getElementById('errormessage').scrollIntoView({
        behavior: 'smooth'
      });
    }, 350);
  }


  showToastErrorMessage(message, title) {
    this.toastrService.error(message, title, {
      timeOut: 3000,
    });
    window.scroll(0,0);
  }

  showToastSuccessMessage(message, title) {
    console.log("hear");
    this.toastrService.success(message, title, {
      timeOut: 3000,
    });
    window.scroll(0,0);
  }
}
