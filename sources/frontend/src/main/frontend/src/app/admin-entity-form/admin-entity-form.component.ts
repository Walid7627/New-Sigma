import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {AdminEntity} from '../../model/admin-entity.model';
import {Address} from '../../model/address.model';
import {AdminEntityService} from '../service/admin-entity.service';
import {CodeAPEService} from '../service/code-ape.service';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {Router} from "@angular/router";
import {CompanyType} from "../../model/provider.model";
//import { formArrayNameProvider } from '@angular/forms/src/directives/reactive_directives/form_group_name';
import { $$ } from 'protractor';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { ToastrService, ToastContainerDirective } from 'ngx-toastr';

@Component({
  selector: 'app-admin-entity-form',
  templateUrl: './admin-entity-form.component.html',
  styleUrls: ['./admin-entity-form.component.css'],
  providers:[AdminEntityService, CodeAPEService]
})

export class AdminsEntityFormComponent implements OnInit {

  admin: AdminEntity = new AdminEntity();
  address: Address = new Address();
  listCodeApe: any;

  registrationSuccessful: boolean = false;
  registrationError: boolean = false;
  adminForm: FormGroup;
  error: string;
  loading: boolean;
  config : PerfectScrollbarConfigInterface = {};
  

  constructor(private adminService:AdminEntityService, private fb: FormBuilder, public dialogRef: MatDialogRef<AdminsEntityFormComponent>,private toastrService: ToastrService) { }

  ngOnInit() {
    this.loading = false;

    this.adminForm = this.fb.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      mail: ['', [Validators.required, Validators.pattern("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")]],
      telephone:  ['', [Validators.required, Validators.pattern("([0-9]{2}\\s){4}[0-9]{2}"), Validators.minLength(14), Validators.maxLength(14)]],
      adresse: this.fb.group({
        number: [''],
        street: ['', Validators.required],
        postalCode: ['', Validators.required],
        city: ['', Validators.required]
      })
    });
    if (this.admin.id) {
      this.adminForm.setValue({
        nom: this.admin.nom,
        prenom: this.admin.prenom,
        mail: this.admin.mail,
        telephone: this.admin.telephone,
        adresse: this.admin.adresse
      });
    }
    
    /*
    this.serviceAPE.getCodeApe().
    subscribe(data => {
      this.listCodeApe=data;
    }, err => {
      console.log(err);
    });*/
  }

  onSubmit({value, valid}: {value: AdminEntity, valid: boolean}) {
    
    console.log(value);
    this.error = "";
    this.registrationError = false;
    this.registrationSuccessful = false;
    this.loading = true;
    console.log("avant submit");
    //this.entityService.addAdminsEntity(this.entity);
    if (this.admin.id == null) {

      this.adminService.save(value)
      .subscribe(res => {
        this.loading = false;
        console.log("entityService data:");
        console.log(res);
        let data:any = res;
        if (data.status === "OK") {
          this.registrationSuccessful = true;
          this.showToastSuccessMessage("L'administrateur d'entité a été ajouté avec succès","Ajout d'administrateur d'entité");
         // this.scrollToSuccessMessage();
          this.adminForm.reset();
        } else {
          this.error = data.message;
          this.registrationError = true
          // this.scrollToErrorMessage();
          this.showToastErrorMessage("Erreur lors de l'ajout de l'administrateur d'entité : "+this.error,"Ajout d'administrateur d'entité");
        }
        console.log("Data:");
        console.log(data);
      }, err => {
        this.loading = false;
        this.error = err;
        this.registrationError = true;
        this.registrationSuccessful = false;
        // this.scrollToErrorMessage();
        this.showToastErrorMessage("Erreur lors de l'ajout de l'administrateur d'entité : "+this.error,"Ajout d'administrateur d'entité");
      });
    } else {
      console.log("modifying:");
      value.id = this.admin.id;

      this.adminService.updateAdminEntity(value)
      .subscribe(res => {
        this.loading = false;
        console.log("adminService data edited:");
        console.log(res);
        let data:any = res;
        if (data.status === "OK") {
          this.registrationSuccessful = true;
          // this.scrollToSuccessMessage();
          this.showToastSuccessMessage("L'administrateur d'entité a été modifier avec succès","Modification d'administrateur d'entité");

        } else {
          this.error = data.message;
          this.registrationError = true
          // this.scrollToErrorMessage();
          this.showToastErrorMessage("Erreur lors de la modification de l'administrateur d'entité : ","Modification d'administrateur d'entité");

        }
        console.log("Data:");
        console.log(data);
      }, err => {
        this.loading = false;
        this.error = err;
        this.registrationError = true;
        this.registrationSuccessful = false;
        this.showToastErrorMessage("Erreur lors de la modification de l'administrateur d'entité : ","Modification d'administrateur d'entité");

      });
    }
  }

  onClose() {    
    this.dialogRef.close();
  }

  /*getAdminsEntity() {
    return this.entity;
  }*/

  // scrollToSuccessMessage() {
  //   setTimeout(() => {
  //     document.getElementById('successmessage').scrollIntoView({
  //       behavior: 'smooth'
  //     });
  //   }, 350);
  // }

  // scrollToErrorMessage() {
  //   setTimeout(() => {
  //     document.getElementById('errormessage').scrollIntoView({
  //       behavior: 'smooth'
  //     });
  //   }, 350);
  // }
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

