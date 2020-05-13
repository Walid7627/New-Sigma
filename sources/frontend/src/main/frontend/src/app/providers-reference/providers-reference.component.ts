
import { map } from 'rxjs/operators';
import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { Provider } from '../../model/provider.model';
import { Address } from '../../model/address.model';
import { ProviderService } from '../service/provider.service';
import { CodeAPEService } from '../service/code-ape.service';
import { CodeCPVService } from '../service/code-cpv.service';
import { FileUploadService } from '../service/file-upload.service';
import { HttpResponse, HttpEventType } from '@angular/common/http';
import { FormControl, FormGroup, Validators, FormBuilder, AbstractControl } from '@angular/forms';
import { MatDialogRef, MatDialogConfig, MatDialog } from '@angular/material/dialog';
import { ProviderContactComponent } from '../provider-contact/provider-contact.component';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { DomSanitizer } from '@angular/platform-browser';

// TODO: Confirmation mdp
// TODO: Champ téléphone vide après inscription

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
  selector: 'providers-reference',
  templateUrl: './providers-reference.component.html',
  styleUrls: ['./providers-reference.component.css'],
  providers: [ProviderService, CodeAPEService]
})
export class ProvidersReferenceComponent implements OnInit {
  // @ViewChild('successmessage') registrationSuccessMessage: ElementRef;
  // @ViewChild('errormessage') registrationErrorMessage: ElementRef;

  address: Address = new Address();
  provider: Provider = new Provider();
  listCodeApe: any;
  listCodeCpv: any;
  listMotherCompany: any;
  registrationSuccessful: boolean = false;
  registrationError: boolean = false;
  error: string;
  action: string;
  user: FormGroup;
  loading: boolean;
  config: PerfectScrollbarConfigInterface = {};
  logoPreview;

  selectedFiles = new Array();
  currentFileUpload: File
  uploadProgress: { percentage: number } = { percentage: 0 };

  logo: FileList;

  @ViewChild(ToastContainerDirective, { static: true }) toastContainer: ToastContainerDirective;

  constructor(public providerService: ProviderService, public serviceAPE: CodeAPEService, public serviceCPV: CodeCPVService,
    private domSanitizer: DomSanitizer, private fb: FormBuilder, private uploadService: FileUploadService, private dialog: MatDialog, public dialogRef: MatDialogRef<ProvidersReferenceComponent>, private toastrService: ToastrService) { }

  ngOnInit() {
    this.toastrService.overlayContainer = this.toastContainer;

    this.loading = false;

    this.listMotherCompany = this.providerService.getAllMotherCompany().pipe(map(result => {

      let items = <any[]>result;
      items.forEach(item => item.providerName = item.nomSociete + " (" + item.numSiret + ")");

      items = items.filter(item => item.id !== this.provider.id);
      return items;
    }));

    this.user = this.fb.group({
      numSiret: ['', [Validators.required, Validators.minLength(14), Validators.maxLength(14)]],
      raisonSociale: ['', Validators.required],
      nomSociete: ['', Validators.required],
      description: [''],
      typeEntreprise: ['TYPE_MAISON_MERE'],
      maisonMere: [null],
      adresse: this.fb.group({
        number: [''],
        street: ['', Validators.required],
        postalCode: ['', Validators.required],
        city: ['', Validators.required],
        country: ['']
      }),
      codeAPE: [null],
      codeCPV: [null],
      logo: [''],
      siteInstitutionnel: [''],
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      telephone: ['', [Validators.required, Validators.pattern("([0-9]{2}\\s){4}[0-9]{2}"), Validators.minLength(14), Validators.maxLength(14)]],
      mobile: ['', [Validators.pattern("([0-9]{2}\\s){4}[0-9]{2}"), Validators.minLength(14), Validators.maxLength(14)]],
      fax: [''],
      mail: ['', [Validators.required, Validators.pattern("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")]],
      confirmmail: ['', [Validators.required, mailConfirming, Validators.pattern("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")]]
    });
    this.action = "L'inscription";
    if (this.provider.id) {

      this.action = "La modification";
      this.user.setValue({
        numSiret: this.provider.numSiret,
        raisonSociale: this.provider.raisonSociale,
        nomSociete: this.provider.nomSociete,
        description: this.provider.description,
        typeEntreprise: this.provider.typeEntreprise,
        maisonMere: this.provider.maisonMere,
        adresse: this.provider.adresse,
        codeAPE: this.provider.codeAPE,
        codeCPV: this.provider.codeCPV,
        logo: this.provider.logo,
        siteInstitutionnel: this.provider.siteInstitutionnel,
        nom: this.provider.nom,
        prenom: this.provider.prenom,
        telephone: this.provider.telephone,
        mobile: this.provider.mobile,
        fax: this.provider.fax,
        mail: this.provider.mail,
        confirmmail: this.provider.mail
      });

      if (this.provider.logo) {
        this.providerService.getLogo(this.provider.logo).subscribe(res => {
          if (res.type === HttpEventType.Response) {
            let documentData: any = res;
            this.logoPreview = URL.createObjectURL(documentData.body);
          }
        });
      }

    }


    this.listCodeApe = this.serviceAPE.getCodeApe().pipe(map(result => {
      const items = <any[]>result;
      items.forEach(item => item.libelleApe = item.codeApe + " - " + item.libelleApe);
      return items;
    }));

    this.listCodeCpv = this.serviceCPV.getCodeCpv().pipe(map(result => {
      const items = <any[]>result;
      items.forEach(item => item.libelleCpv = item.codeCpv + " - " + item.libelleCpv);
      return items;
    }));
  }

  onClose() {
    this.dialogRef.close();
  }


  onSubmit({ value, valid }: { value: Provider, valid: boolean }) {
    this.error = "";
    this.registrationError = false;
    this.registrationSuccessful = false;
    this.loading = true;

    if (!this.provider.id) {
      this.providerService.save(value)
        .subscribe(res => {
          this.loading = false;
          let data: any = res;
          if (data.status === "OK") {
            if (this.logo) {
              this.uploadLogo(value.mail).subscribe(event => {
                if (event.type === HttpEventType.Response) {
                  let data: any = event.body;
                  if (data.status !== "OK") {
                    console.log("Error while uploading logo")
                    this.error = data.message;
                    this.registrationError = true;
                    return;
                  } else {
                    this.provider.logo = data.message;
                    this.providerService.getLogo(this.provider.logo).subscribe(res => {
                      if (res.type === HttpEventType.Response) {
                        let documentData: any = res;
                        this.logoPreview = URL.createObjectURL(documentData.body);
                      }
                    });
                  }
                }
              });
              console.log("uploadLogo called");
            }

            if (this.selectedFiles.length > 0) {
              this.selectedFiles.forEach(file => {
                this.upload(value.mail, file).subscribe(event => {
                  this.selectedFiles = undefined;

                  if (event.type === HttpEventType.UploadProgress) {
                    this.uploadProgress.percentage = Math.round(100 * event.loaded / event.total);
                  } else if (event.type === HttpEventType.Response) {
                    console.log("Upload done!\n");
                    console.log(event);

                    let data: any = event.body;

                    if (data.status === "OK") {
                      console.log("Success upload file");
                      this.provider = JSON.parse(data.message);
                    } else {
                      this.error = data.message;
                      console.log("UnSuccess upload file : " + this.error);
                    }
                  }
                });
              });

            } else {
              this.registrationSuccessful = true;
              this.provider = JSON.parse(data.message);
            }

            this.showToastSuccessMessage("Fournisseur ajouté avec succès", "Inscription d'un fournisseur");
          } else {
            this.error = data.message;
            this.registrationError = true;
            this.showToastErrorMessage("Erreur lors de l'ajout du fournisseur", "Inscription d'un fournisseur");
          }

        }, err => {
          this.loading = false;

          this.error = err;
          this.registrationError = true;
          this.registrationSuccessful = false;
          this.showToastErrorMessage("Erreur lors de l'ajout du fournisseur", "Inscription d'un fournisseur");
        });
    } else {
      value.id = this.provider.id;
      value.maisonMere = (value.typeEntreprise == 'TYPE_MAISON_MERE') ? null : value.maisonMere;
      this.providerService.updateProvider(value)
        .subscribe(res => {
          this.loading = false;

          let data: any = res;
          if (data.status === "OK") {
            if (this.logo) {
              this.uploadLogo(this.provider.mail).subscribe(event => {
                if (event.type === HttpEventType.Response) {
                  let data: any = event.body;
                  console.log("uploadLogo called");
                  console.log("message logo = " + data.message);
                  if (data.status !== "OK") {
                    console.log("Error while uploading logo")
                    this.error = data.message;

                    return;
                  } else {
                    this.registrationSuccessful = true;
                    this.provider.logo = data.message;
                    this.providerService.getLogo(this.provider.logo).subscribe(res => {
                      if (res.type === HttpEventType.Response) {
                        let documentData: any = res;
                        this.logoPreview = URL.createObjectURL(documentData.body);
                      }
                    });
                  }
                }
              });
              this.showToastSuccessMessage("Fournisseur modifié avec succès", "Modification d'un fournisseur");

            } else {
              this.registrationSuccessful = true;
              this.provider = JSON.parse(data.message);
              this.showToastSuccessMessage("Fournisseur ajouté avec succès", "Modification d'un fournisseur");
            }

          } else {
            this.error = data.message;
            this.registrationError = true
            this.showToastErrorMessage("Erreur lors de la modification du fournisseur", "Modification d'un fournisseur");
          }
          console.log("Data:");
          console.log(data);
        }, err => {
          this.loading = false;
          this.error = err;
          this.registrationError = true;
          this.registrationSuccessful = false;
          this.showToastErrorMessage("Erreur lors de la modification du fournisseur", "Modification d'un fournisseur");
        });
    }

  }

  selectFile(event) {
    let list: FileList = event.target.files;
    console.log(list);

    for (let i = 0; i < list.length; i++) {
      this.selectedFiles.push(list[i]);
    }

  }

  upload(user_credentials, file) {
    this.uploadProgress.percentage = 0;
    return this.uploadService.upload(file, user_credentials, "TYPE_DOCUMENT");
  }
  selectLogo(event) {
    console.log("selectLogo called");
    this.logo = event.target.files;
  }


  uploadLogo(user_credentials) {
    return this.uploadService.upload(this.logo.item(0), user_credentials, "TYPE_LOGO");
  }

  showContacts() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "80%";
    const dialogRef = this.dialog.open(ProviderContactComponent, dialogConfig);
    dialogRef.componentInstance.provider = this.provider;
    //CORRECTION BUG
    dialogRef.componentInstance.loadData();
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  showToastErrorMessage(message, title) {
    this.toastrService.error(message, title, {
      timeOut: 3000,
    });
    window.scroll(0, 0);
  }

  showToastSuccessMessage(message, title) {
    this.toastrService.success(message, title, {
      timeOut: 3000,
    });
    window.scroll(0, 0);
  }

}
