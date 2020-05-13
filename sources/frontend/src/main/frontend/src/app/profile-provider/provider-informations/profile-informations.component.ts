import { Component, OnInit, ViewChild } from "@angular/core";
import { AuthService } from "../../core/auth/auth.service";
import { ProfileProviderService } from '../profile-provider.service';
import { DomSanitizer } from '@angular/platform-browser';
import { HttpEventType } from '@angular/common/http';
import { Provider } from '../../../model/provider.model';
import { Observable } from 'rxjs';
import { CodeCPVService } from '../../service/code-cpv.service';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { ProviderService } from '../../service/provider.service';
import { FileUploadService } from '../../service/file-upload.service';
import { CodeAPEService } from '../../service/code-ape.service';
import { NgSelectModule } from '@ng-select/ng-select';
import { APE } from '../../../model/ape.model';
import { CPV } from '../../../model/cpv.model';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-provider-informations',
  templateUrl: './profile-informations.component.html',
  styleUrls: ['./profile-informations.component.css']
})

export class ProfileInformationsComponent implements OnInit {

  //address: Address = new Address();
  provider: Provider = new Provider();
  listCodeApe: any;
  listCodeCpv: Observable<any>;

  updateSuccessful: boolean = false;
  updateError: boolean = false;
  error: string;
  user: FormGroup;
  loading: boolean;
  listMotherCompany: any;

  //selectedFiles: FileList
  //currentFileUpload: File
  //uploadProgress: { percentage: number } = { percentage: 0 };

  logo: FileList;

  logoPreview;

  @ViewChild(ToastContainerDirective) toastContainer: ToastContainerDirective;

  constructor(private profileProviderService: ProfileProviderService, public providerService:ProviderService, public serviceAPE:CodeAPEService,
              public serviceCPV:CodeCPVService, private fb: FormBuilder, private uploadService: FileUploadService,
              private domSanitizer: DomSanitizer, private toastrService: ToastrService) {
    this.user = this.fb.group({
      numSiret: ['', [Validators.required, Validators.minLength(14), Validators.maxLength(14)]],
      raisonSociale: ['', Validators.required],
      nomSociete: ['', Validators.required],
      description: [''],
      typeEntreprise: ['TYPE_MAISON_MERE'],
      maisonMere:[null],
      adresse: this.fb.group({
        street: ['', Validators.required],
        city: ['', Validators.required],
        postalCode: ['', Validators.required]
      }),
      codeAPE: [''],
      codeCPV: [''],
      logo: [''],
      siteInstitutionnel: [''],
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      telephone:  ['', [Validators.required, Validators.pattern("([0-9]{2}\\s){4}[0-9]{2}"), Validators.minLength(14), Validators.maxLength(14)]],
      mobile: ['', [Validators.pattern("([0-9]{2}\\s){4}[0-9]{2}"), Validators.minLength(14), Validators.maxLength(14)]],
      fax: [''],
      adressePhysique: ['']
    });
  }

  ngOnInit() {
    //this.provider.maisonMere = null;
    //this.provider.adresse = this.address;
    //this.loading = false;
    this.loadData();
    this.toastrService.overlayContainer = this.toastContainer;

    this.listMotherCompany = this.providerService.getAllMotherCompany().pipe(map(result => {

      let items =<any[]>result;
      items.forEach(item => item.providerName = item.nomSociete + " (" + item.numSiret + ")");
    
      items = items.filter(item => item.id !== this.provider.id);
      return items;
    }));

    this.listCodeApe = this.serviceAPE.getCodeApe().map(result => {
      const items = <any[]>result;
      console.log(result);
      items.forEach(item => item.libelleApe = item.codeApe + " - " + item.libelleApe);
      return items;
    });

    console.log("Logging getCodeCpv map result:");
    this.listCodeCpv = this.serviceCPV.getCodeCpv().map(result => {
      console.log(result);
      const items = <any[]>result;
      items.forEach(item => item.libelleCpv = item.codeCpv + " - " + item.libelleCpv);
      return items;
    });
  }

  loadData() {

    this.profileProviderService.get().subscribe(
      event => {
        if (event.type === HttpEventType.Response) {
          let data:any = event.body;

          if (data.status === "OK") {
            this.provider = JSON.parse(data.message);

            if (this.provider.logo) {
              this.profileProviderService.getDocument(this.provider.logo).subscribe(res => {
                if(res.type === HttpEventType.Response) {
                  let documentData: any = res;
                  this.logoPreview = URL.createObjectURL(documentData.body);
                }
              });
            }


            this.user = this.fb.group({
              numSiret: [this.provider.numSiret, [Validators.required, Validators.minLength(14), Validators.maxLength(14)]],
              raisonSociale: [this.provider.raisonSociale, Validators.required],
              nomSociete: [this.provider.nomSociete, Validators.required],
              description: [this.provider.description],
              typeEntreprise: [this.provider.typeEntreprise],
              maisonMere: [this.provider.maisonMere],
              adresse: this.fb.group({
                street: [this.provider.adresse.street, Validators.required],
                city: [this.provider.adresse.city, Validators.required],
                postalCode: [this.provider.adresse.postalCode, Validators.required]
              }),
              codeAPE: [this.provider.codeAPE],
              codeCPV: [this.provider.codeCPV],
              logo: [this.provider.logo],
              siteInstitutionnel: [this.provider.siteInstitutionnel],
              nom: [this.provider.nom, Validators.required],
              prenom: [this.provider.prenom, Validators.required],
              telephone: [this.provider.telephone],
              mobile:[this.provider.mobile],
              fax: [this.provider.fax],
              adressePhysique: [this.provider.adressePhysique]
            });

            // this.user.controls['numSiret'].setValue(this.provider.numSiret);

            console.log("Provider loaded", this.provider);
            } else {
            console.log("Error while loading provider");
            console.log(data);
          }
        }

      },

      err => {
        console.log("Error while loading provider");
        console.log(err);
      }
    );
  }

  submit({ value, valid }: { value: Provider, valid: boolean }) {
    this.updateSuccessful = false;
    this.updateError = false;
    this.loading = true;
    this.error = "";

    console.log("Submitting:", value);
    this.logoPreview = null;
    value.id = this.provider.id;
    this.profileProviderService.update(value).subscribe(
      res => {
        console.log("Response:", res);

        if (res.type === HttpEventType.Response) {

          let data:any = res.body;

          if (data.status === "OK") {
            if (this.logo) {
              this.uploadLogo(this.provider.mail).subscribe(event => {
                console.log("uploadLogo called");
                if (event.type === HttpEventType.Response) {
                  let data: any = event.body;
                  if (data.status !== "OK") {
                    console.log("Error while uploading logo")
                    this.error = data.message;
                    this.updateError = true;
                    return;
                  } else {
                    this.updateSuccessful = true;
                    this.provider.logo = data.message;
                    this.providerService.getLogo(this.provider.logo).subscribe(res => {
                      if (res.type === HttpEventType.Response) {
                        let documentData: any = res;
                        this.logoPreview = URL.createObjectURL(documentData.body);
                      }
                    });
                    this.showToastSuccessMessage("Mise à jour des informations effectuée avec succès", "Informations du fournisseur");
                  }
                }
              });
            } else {
              this.updateSuccessful = true;
              this.showToastSuccessMessage("Mise à jour des informations effectuée avec succès", "Informations du fournisseur");
            }
            console.log("Reload data after update");
            setTimeout(() => {
              this.loadData();
            }, 250);

          } else {
            this.updateError = true;
            this.showToastErrorMessage("Erreur lors de la mise à jour des informations", "Informations du fournisseur");
            this.error = data.message;
          }

          this.loading = false;
        }
      },
      err => {
        this.updateError = true;
        this.showToastErrorMessage("Erreur lors de la mise à jour des informations", "Informations du fournisseur");
        this.error = err;
        this.loading = false;
      }
    );
  }

  selectLogo(event) {
    console.log("selectLogo called");
    this.logo = event.target.files;
  }

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
