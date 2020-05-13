import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {Visiteur} from '../../model/visiteur.model';
import {Address} from '../../model/address.model';
import {VisiteurService} from '../service/visiteur.service';
import {CodeAPEService} from '../service/code-ape.service';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {Router} from "@angular/router";
import {CompanyType} from "../../model/provider.model";
//import { formArrayNameProvider } from '@angular/forms/src/directives/reactive_directives/form_group_name';
import { $$ } from 'protractor';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { ToastrService, ToastContainerDirective } from 'ngx-toastr';

@Component({
  selector: 'app-visiteur-form',
  templateUrl: './visiteur-form.component.html',
  styleUrls: ['./visiteur-form.component.css'],
  providers:[VisiteurService, CodeAPEService]
})

export class VisiteursFormComponent implements OnInit {

  visiteur: Visiteur = new Visiteur();
  address: Address = new Address();
  listCodeApe: any;

  registrationSuccessful: boolean = false;
  registrationError: boolean = false;
  visiteurForm: FormGroup;
  error: string;
  loading: boolean;
  config : PerfectScrollbarConfigInterface = {};
  

  constructor(private visiteurService:VisiteurService, private fb: FormBuilder, public dialogRef: MatDialogRef<VisiteursFormComponent>,private toastrService: ToastrService) { }

  ngOnInit() {
    this.loading = false;

    this.visiteurForm = this.fb.group({
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
    if (this.visiteur.id) {
      this.visiteurForm.setValue({
        nom: this.visiteur.nom,
        prenom: this.visiteur.prenom,
        mail: this.visiteur.mail,
        telephone: this.visiteur.telephone,
        adresse: this.visiteur.adresse
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

  onSubmit({value, valid}: {value: Visiteur, valid: boolean}) {
    
    console.log(value);
    this.error = "";
    this.registrationError = false;
    this.registrationSuccessful = false;
    this.loading = true;
    console.log("avant submit");
    //this.entityService.addVisiteurs(this.entity);
    if (this.visiteur.id == null) {

      this.visiteurService.save(value)
      .subscribe(res => {
        this.loading = false;
        console.log("entityService data:");
        console.log(res);
        let data:any = res;
        if (data.status === "OK") {
          this.registrationSuccessful = true;
          this.showToastSuccessMessage("Le visiteur a été ajouté avec succès","Ajout du visiteur");
         // this.scrollToSuccessMessage();
          this.visiteurForm.reset();
        } else {
          this.error = data.message;
          this.registrationError = true
          // this.scrollToErrorMessage();
          this.showToastErrorMessage("Erreur lors de l'ajout du visiteur : "+this.error,"Ajout ddu visiteur");
        }
        console.log("Data:");
        console.log(data);
      }, err => {
        this.loading = false;
        this.error = err;
        this.registrationError = true;
        this.registrationSuccessful = false;
        // this.scrollToErrorMessage();
        this.showToastErrorMessage("Erreur lors de l'ajout du visiteur : "+this.error,"Ajout du visiteur");
      });
    } else {
      console.log("modifying:");
      value.id = this.visiteur.id;

      this.visiteurService.updateVisiteur(value)
      .subscribe(res => {
        this.loading = false;
        console.log("visiteurService data edited:");
        console.log(res);
        let data:any = res;
        if (data.status === "OK") {
          this.registrationSuccessful = true;
          // this.scrollToSuccessMessage();
          this.showToastSuccessMessage("Le visiteur a été modifier avec succès","Modification du visiteur");

        } else {
          this.error = data.message;
          this.registrationError = true
          // this.scrollToErrorMessage();
          this.showToastErrorMessage("Erreur lors de la modification de le visiteur : ","Modification du visiteur");

        }
        console.log("Data:");
        console.log(data);
      }, err => {
        this.loading = false;
        this.error = err;
        this.registrationError = true;
        this.registrationSuccessful = false;
        this.showToastErrorMessage("Erreur lors de la modification du visiteur : ","Modification du visiteur");

      });
    }
  }

  onClose() {    
    this.dialogRef.close();
  }

  /*getVisiteurs() {
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

