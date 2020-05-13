import {Component, OnInit, ViewChild} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {Entity} from '../../model/entity.model';
import {Address} from '../../model/address.model';
import {EntityService} from '../service/entity.service';
import {CodeAPEService} from '../service/code-ape.service';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {CompanyType} from "../../model/provider.model";
import { AdminEntityService } from '../service/admin-entity.service';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { map } from 'rxjs/operators';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-entity-form',
  templateUrl: './entity-form.component.html',
  styleUrls: ['./entity-form.component.css'],
  providers:[EntityService, CodeAPEService]
})

export class EntityFormComponent implements OnInit {

  entity: Entity = new Entity();
  address: Address = new Address();
  listCodeApe: any;
  listFreeAdmin : any;
  registrationSuccessful: boolean = false;
  registrationError: boolean = false;
  entityForm: FormGroup;
  error: string;
  loading: boolean;
  adminName:string;

  config : PerfectScrollbarConfigInterface = {};

  companyTypeArray:CompanyType[] =[CompanyType.TYPE_MAISON_MERE,CompanyType.TYPE_SUCCURSALE,CompanyType.TYPE_FILIALE];

  @ViewChild(ToastContainerDirective, { static: true }) toastContainer: ToastContainerDirective;
  constructor(private entityService:EntityService, public serviceAPE:CodeAPEService, private fb: FormBuilder, public dialogRef: MatDialogRef<EntityFormComponent>, private adminService : AdminEntityService, private toastrService: ToastrService) { }

  ngOnInit() {
    this.toastrService.overlayContainer = this.toastContainer;
    this.loading = false;
   
    this.entityForm = this.fb.group({
      numSiret: ['', [Validators.required, Validators.minLength(14), Validators.maxLength(14)]],
      raisonSociale: ['', Validators.required],
      nomSociete: ['', Validators.required],
      typeEntreprise: ['TYPE_MAISON_MERE', Validators.required],
      maisonMere: [''], 
      telephone:  ['', [Validators.required, Validators.pattern("([0-9]{2}\\s){4}[0-9]{2}"), Validators.minLength(14), Validators.maxLength(14)]],
      adresse: this.fb.group({
        number: [''],
        street: ['', Validators.required],
        postalCode: ['', Validators.required],
        city: ['', Validators.required],
        country: ['']
      }),
      codeAPE: [null],
      administrateur: [null]
    });
    
    if (this.entity.id) {
      console.log(this.entity.maisonMere);
      this.entityForm.setValue({
        numSiret: this.entity.numSiret,
        raisonSociale: this.entity.raisonSociale,
        nomSociete: this.entity.nomSociete,
        typeEntreprise: this.entity.typeEntreprise,
        maisonMere: this.entity.maisonMere,
        telephone: this.entity.telephone,
        adresse: this.entity.adresse,
        administrateur: this.entity.administrateur,
        codeAPE: this.entity.codeAPE
      });
      this.adminName = (this.entity.administrateur) ? this.entity.administrateur.prenom + " " + this.entity.administrateur.nom : "";
      console.log(this.adminName);
    }
    this.listCodeApe = this.serviceAPE.getCodeApe().pipe(map(result => {
      const items = <any[]>result;
      console.log(result);
      items.forEach(item => item.libelleApe = item.codeApe + " - " + item.libelleApe);
      return items;
    }));

    this.listFreeAdmin = this.adminService.getFreeAdmins().pipe(map(result => {

      const items =<any[]>result;
      console.log(items);
      //items.push(result[0].prenom);
      items.forEach(item => item.administrateurName = item.nom + " - " + item.prenom + "   (" + item.mail + ")");
      
      return items;
    }));
    
  }

  onSubmit({value, valid}: {value: Entity, valid: boolean}) {
    
    this.error = "";
    this.registrationError = false;
    this.registrationSuccessful = false;
    this.loading = true;
    if (this.entity.numSiret == null) {
      this.entityService.save(value)
      .subscribe(res => {
        this.loading = false;
        let data:any = res;
        if (data.status === "OK") {
          this.registrationSuccessful = true;
          this.entity = JSON.parse(data.message);
          this.showToastSuccessMessage("Entité ajoutée avec succès", "Ajout d'une entité");
        } else {
          this.error = data.message;
          this.registrationError = true
          this.showToastErrorMessage("Erreur lors de l'ajout de l'entité", "Ajout d'une entité");
        }
        console.log("Data:");
        console.log(data);
      }, err => {
        this.loading = false;
        this.error = err;
        this.registrationError = true;
        this.registrationSuccessful = false;
        this.showToastErrorMessage("Erreur lors de l'ajout de l'entité", "Ajout d'une entité");
      });
    } else {
      console.log("modifying:");
      value.id = this.entity.id;
      value.administrateur = null;
      this.entityService.updateEntity(value)
      .subscribe(res => {
        this.loading = false;
        console.log("entityService data edited:");
        let data:any = res;
        if (data.status === "OK") {
          this.registrationSuccessful = true;
          this.showToastSuccessMessage("Entité modifiée avec succès", "Modification d'une entité");
        } else {
          this.error = data.message;
          this.registrationError = true
          this.showToastErrorMessage("Erreur lors de la modification de l'entité", "Modification d'une entité");
        }
        console.log("Data:");
        console.log(data);
      }, err => {
        this.loading = false;
        this.error = err;
        this.registrationError = true;
        this.registrationSuccessful = false;
        this.showToastErrorMessage("Erreur lors de la modification de l'entité", "Modification d'une entité");
      });
    }
  }

  onClose() {    
    this.dialogRef.close();
  }

  /*getEntity() {
    return this.entity;
  }*/

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

