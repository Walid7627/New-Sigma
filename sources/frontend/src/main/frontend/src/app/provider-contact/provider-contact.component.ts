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
import { ProfileProviderService } from '../profile-provider/profile-provider.service';
import { DialogService } from '../service/dialog-service'; 
import { UserStorage } from '../core/userstorage/user.storage';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { MatDialogRef } from '@angular/material/dialog';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { RoleService } from '../core/role/role.service';



@Component({
  selector: 'app-provider-contact',
  templateUrl: './provider-contact.component.html',
  styleUrls: ['./provider-contact.component.css']
})
export class ProviderContactComponent implements OnInit {

  provider;
  config : PerfectScrollbarConfigInterface = {};

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
,private dialogService : DialogService, public dialogRef: MatDialogRef<ProviderContactComponent>,private roleService: RoleService) {
  }

  ngOnInit() {
    this.toastrService.overlayContainer = this.toastContainer;
    this.user = this.fb.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      telephone:  ['', [Validators.required, Validators.pattern("([0-9]{2}\\s){4}[0-9]{2}"), Validators.minLength(14), Validators.maxLength(14)]],
      mobile:  ['', [Validators.pattern("([0-9]{2}\\s){4}[0-9]{2}"), Validators.minLength(14), Validators.maxLength(14)]],
      fax: [''],
      mail: ['', [Validators.required, Validators.pattern("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")]],
      // confirmmail: ['', [Validators.required, mailConfirming, Validators.pattern("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")]],
      // password: ['', Validators.required],
      // confirmpassword: ['', [Validators.required, passwordConfirming]],
      adresse: ['', Validators.required],
    });
    
  }
  IamNotVisiteur() {
    return (!this.roleService.canActivate("ROLE_VISITEUR"));

  }

  loadData() {
    
      this.profileProviderService.getByMail(this.provider.mail).subscribe(
        event => {
        if (event.type === HttpEventType.Response) {
          let data:any = event.body;
  
          if (data.status === "OK") {
            this.provider = JSON.parse(data.message);
            console.log(this.provider);
          }
        }
      });
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
  onSubmit({ value, valid }) {
    
    this.profileProviderService.addContact(value, this.provider)
    .subscribe(response => {
      if (response.type === HttpEventType.Response) {
        let data:any = response.body;
        console.log(data);
        if (data.status === "OK") {
          this.loadData();
	  this.showToastSuccessMessage("Contact ajouté avec succès", "Ajout de contact");
	  this.user.reset();
        } else {
          console.log("Error while adding new contact");
          this.showToastErrorMessage("Contact non ajouté : email existe déjà", "Ajout de contact");
        }
      }
    }, error => {
      console.log('Error: ');
      console.log(error);
    });
  }

  onClose() {    
    this.dialogRef.close();
  }

  removeContact(contact) {
    console.log("Removing:", contact);
  
    
this.dialogService.openConfirmDialog('Etes-vous sûr de vouloir supprimer ce contact ?')
    .afterClosed().subscribe(res =>{
      if(res){
        this.profileProviderService.removeContact(contact.id, this.provider).subscribe(
      response => {
        console.log("Remove contact response", response);
	
        if (response.type === HttpEventType.Response) {
          let data:any = response.body;
          if (data.status === "OK") {
            this.loadData();
	this.showToastSuccessMessage("Contact supprimé avec succès", "Suppression de contact");
            console.log("Contact removed successfully");
          } else {
            console.log("Error when removing contact", data.message);
          }
        }
      },
      err => {
        console.log("Error when removing contact", err);
      }
    )
        
      }
    });
   
  }
}
