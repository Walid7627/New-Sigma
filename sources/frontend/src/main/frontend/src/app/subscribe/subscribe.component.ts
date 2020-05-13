import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { Provider } from '../../model/provider.model';
import { Address } from '../../model/address.model';
import { ProviderService } from '../service/provider.service';
import { CodeAPEService } from '../service/code-ape.service';
import { CodeCPVService } from '../service/code-cpv.service';
import { FileUploadService } from '../service/file-upload.service';
import { HttpResponse, HttpEventType } from '@angular/common/http';
import { FormControl, FormGroup, Validators, FormBuilder, FormsModule, AbstractControl } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { Observable } from 'rxjs';
import 'rxjs/add/observable/from';
import 'rxjs/add/operator/map';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';

import * as Rx from 'rxjs';

function passwordConfirming(c: AbstractControl): any {
  if (!c.parent || !c) return;
  const pwd = c.parent.get('password');
  const cpwd = c.parent.get('confirmpassword')

  if (!pwd || !cpwd) return;
  if (pwd.value !== cpwd.value) {
    return { invalid: true };

  }
}

function mailConfirming(c: AbstractControl): any {
  if (!c.parent || !c) return;
  const mail = c.parent.get('mail');
  const cmail = c.parent.get('confirmmail')

  if (!mail || !cmail) return;
  if (mail.value !== cmail.value) {
    return { invalid: true };

  }
}
@Component({
  selector: 'app-subscribe',
  templateUrl: './subscribe.component.html',
  styleUrls: ['./subscribe.component.css']
})
export class SubscribeComponent implements OnInit {
  // @ViewChild('successmessage') registrationSuccessMessage: ElementRef;
  // @ViewChild('errormessage') registrationErrorMessage: ElementRef;

  address: Address = new Address();
  provider: Provider = new Provider();
  listCodeApe: any;
  listCodeCpv: Observable<any>;
  listMotherCompany: any;

  registrationSuccessful: boolean = false;
  registrationError: boolean = false;
  error: string;
  user: FormGroup;
  loading: boolean;

  selectedFiles = new Array();
  currentFileUpload: File
  uploadProgress: { percentage: number } = { percentage: 0 };

  logo: FileList;
  get cpwd() {
    return this.user.get('confirmpassword');
  }

  logoPreview;

  listeCodeCPV;

  @ViewChild(ToastContainerDirective, { static: true }) toastContainer: ToastContainerDirective;
  constructor(public providerService: ProviderService, public serviceAPE: CodeAPEService,
    public serviceCPV: CodeCPVService, private fb: FormBuilder, private uploadService: FileUploadService, private toastrService: ToastrService) { }

  ngOnInit() {
    this.provider.maisonMere = null;
    this.provider.adresse = this.address;
    this.loading = false;
    this.toastrService.overlayContainer = this.toastContainer;
    this.listMotherCompany = this.providerService.getAllMotherCompany().map(result => {

      let items =<any[]>result;
      items.forEach(item => item.providerName = item.nomSociete + " (" + item.numSiret + ")");
    
      items = items.filter(item => item.id !== this.provider.id);
      return items;
    });

    this.user = this.fb.group({
      numSiret: ['', [Validators.required, Validators.minLength(14), Validators.maxLength(14)]],
      raisonSociale: ['', Validators.required],
      nomSociete: ['', Validators.required],
      description: [''],
      maisonMere: [null],
      typeEntreprise: ['TYPE_MAISON_MERE'],
      adresse: this.fb.group({
        street: ['', Validators.required],
        city: ['', Validators.required],
        postalCode: ['', Validators.required]
      }),
      codeAPE: [null],
      codeCPV: [null],
      logo: [''],
      siteInstitutionnel: [''],
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      telephone:  ['', [Validators.required, Validators.pattern("([0-9]{2}\\s){4}[0-9]{2}"), Validators.minLength(14), Validators.maxLength(14)]],
      mobile:  ['', [Validators.required, Validators.pattern("([0-9]{2}\\s){4}[0-9]{2}"), Validators.minLength(14), Validators.maxLength(14)]],
      fax: [''],
      mail: ['', [Validators.required, Validators.pattern("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")]],
      confirmmail: ['', [Validators.required, mailConfirming, Validators.pattern("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")]],

      password: ['', [Validators.required, Validators.minLength(8)]],
      confirmpassword: ['', [Validators.required, passwordConfirming]],
      adressePhysique: ['']
    });

    this.listCodeApe = this.serviceAPE.getCodeApe().map(result => {
      const items = <any[]>result;
      items.forEach(item => item.libelleApe = item.codeApe + " - " + item.libelleApe);
      return items;
    });

    this.listCodeCpv = this.serviceCPV.getCodeCpv().map(result => {
      const items = <any[]>result;
      items.forEach(item => item.libelleCpv = item.codeCpv + " - " + item.libelleCpv);
      return items;
    });
  }


  onSubmit({ value, valid }: { value: Provider, valid: boolean }) {
    this.error = "";
    this.registrationError = false;
    this.registrationSuccessful = false;
    this.loading = true;

    console.log("Value: \n");
    console.log(value);
    console.log("Valid: " + valid);
    this.providerService.save(value)
      .subscribe(res => {
        this.loading = false;
        console.log("providerService data:");
        console.log(res);
        let data: any = res;
        if (data.status === "OK") {
          this.user.reset();
          if (this.logo) {
            this.uploadLogo(value.mail).subscribe(event => {
              if (event.type === HttpEventType.Response) {
                if (data.status !== "OK") {
                  console.log("Error while uploading logo")
                  this.error = data.message;
                  this.registrationError = true;
                  return;
                }
              }
            });
            console.log("uploadLogo called");
          }

          if (this.selectedFiles) {
            this.selectedFiles.forEach(file => {
              this.upload(value.mail, file).subscribe(event => {
  
                if (event.type === HttpEventType.UploadProgress) {
                  this.uploadProgress.percentage = Math.round(100 * event.loaded / event.total);
                } else if (event.type === HttpEventType.Response) {
                  console.log("Upload done!\n");
                  console.log(event);
  
                  let data: any = event.body;
  
                  if (data.status === "OK") {
                    console.log("Success upload : " + file);
                   
                  } else {
                    this.error = data.message;
                    console.log(this.error + ": " + file);
                  }
                }
              });
            });
            this.showToastSuccessMessage("Un lien d'activation a été envoyé dans votre boite mail. Vous devez activer votre compte avant de pouvoir vous connecter.","Inscription effectuée avec succès" );
            
          } else {
            this.registrationSuccessful = true;
            this.showToastSuccessMessage("Un lien d'activation a été envoyé dans votre boite mail. Vous devez activer votre compte avant de pouvoir vous connecter.","Inscription effectuée avec succès" );
          }
        } else {
          this.error = data.message;
          this.registrationError = true;
          this.showToastErrorMessage("Erreur lors de l'inscription","Inscription d'un fournisseur" );
        }
        console.log("Data:");
        console.log(data);
      }, err => {
        this.loading = false;

        this.error = err;
        this.registrationError = true;
        this.registrationSuccessful = false;
        this.showToastErrorMessage("Erreur lors de l'inscription","Inscription d'un fournisseur" );
      });
  }
  selectLogo(event) {
    console.log("selectLogo called");
    this.logo = event.target.files;
  }

  selectFile(event) {
    let list : FileList = event.target.files;
    console.log(list);
    
    for (let i = 0;i < list.length; i++) {
      this.selectedFiles.push(list[i]);
    }
    
  }

  upload(user_credentials, file) {
    this.uploadProgress.percentage = 0;
    return this.uploadService.upload(file, user_credentials, "TYPE_DOCUMENT");
  }

  /* upload(user_credentials) {
    this.uploadProgress.percentage = 0;

    this.currentFileUpload = this.selectedFiles.item(0);
  
    this.uploadService.upload(this.currentFileUpload, user_credentials, "TYPE_DOCUMENT").subscribe(event => {

        this.selectedFiles = undefined;

        if (event.type === HttpEventType.UploadProgress) {
          this.uploadProgress.percentage = Math.round(100 * event.loaded / event.total);
        } else if (event.type === HttpEventType.Response){
          console.log("Upload done!\n");
          console.log(event);
        }
    });
  } */

  uploadLogo(user_credentials) {
    return this.uploadService.upload(this.logo.item(0), user_credentials, "TYPE_LOGO");
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
