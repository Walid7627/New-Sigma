import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {Purchaser} from '../../model/purchaser.model';
import {Address} from '../../model/address.model';
import {PurchaserService} from '../service/purchaser.service';
import {CodeAPEService} from '../service/code-ape.service';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {Router} from "@angular/router";
import {CompanyType} from "../../model/provider.model";
//import { formArrayNameProvider } from '@angular/forms/src/directives/reactive_directives/form_group_name';
import { $$ } from 'protractor';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { ToastrService, ToastContainerDirective } from 'ngx-toastr';

@Component({
  selector: 'app-purchaser-form',
  templateUrl: './purchaser-form.component.html',
  styleUrls: ['./purchaser-form.component.css'],
  providers:[PurchaserService, CodeAPEService]
})

export class PurchaserFormComponent implements OnInit {

  purchaser: Purchaser = new Purchaser();
  address: Address = new Address();
  listCodeApe: any;

  registrationSuccessful: boolean = false;
  registrationError: boolean = false;
  purchaserForm: FormGroup;
  error: string;
  loading: boolean;
  config : PerfectScrollbarConfigInterface = {};
  

  constructor(private purchaserService:PurchaserService, private fb: FormBuilder, public dialogRef: MatDialogRef<PurchaserFormComponent>,private toastrService: ToastrService) { }

  ngOnInit() {
    this.loading = false;

    this.purchaserForm = this.fb.group({
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
    if (this.purchaser.id) {
      this.purchaserForm.setValue({
        nom: this.purchaser.nom,
        prenom: this.purchaser.prenom,
        mail: this.purchaser.mail,
        telephone: this.purchaser.telephone,
        adresse: this.purchaser.adresse
      });
    }
  }

  onSubmit({value, valid}: {value: Purchaser, valid: boolean}) {
    
    console.log(value);
    this.error = "";
    this.registrationError = false;
    this.registrationSuccessful = false;
    this.loading = true;
    console.log("avant submit");
   
    if (this.purchaser.id == null) {

      this.purchaserService.save(value)
      .subscribe(res => {
        this.loading = false;
        console.log("Service data:");
        console.log(res);
        let data:any = res;
        if (data.status === "OK") {
          this.registrationSuccessful = true;
          this.showToastSuccessMessage("L'acheteur a été ajouté avec succès","Ajout d'acheteur");
         // this.scrollToSuccessMessage();
          this.purchaserForm.reset();
        } else {
          this.error = data.message;
          this.registrationError = true
          // this.scrollToErrorMessage();
          this.showToastErrorMessage("Erreur lors de l'ajout de l'acheteur : "+this.error,"Ajout d'acheteur");
        }
        console.log("Data:");
        console.log(data);
      }, err => {
        this.loading = false;
        this.error = err;
        this.registrationError = true;
        this.registrationSuccessful = false;
        // this.scrollToErrorMessage();
        this.showToastErrorMessage("Erreur lors de l'ajout de l'acheteur: "+this.error,"Ajout d'acheteur");
      });
    } else {
      console.log("modifying:");
      value.id = this.purchaser.id;

      this.purchaserService.updatePurchaser(value)
      .subscribe(res => {
        this.loading = false;
        console.log("purchaserService data edited:");
        console.log(res);
        let data:any = res;
        if (data.status === "OK") {
          this.registrationSuccessful = true;
          // this.scrollToSuccessMessage();
          this.showToastSuccessMessage("L'acheteur a été modifier avec succès","Modification d'acheteur");

        } else {
          this.error = data.message;
          this.registrationError = true
          // this.scrollToErrorMessage();
          this.showToastErrorMessage("Erreur lors de la modification de l'acheteur : ","Modification d'acheteur");

        }
        console.log("Data:");
        console.log(data);
      }, err => {
        this.loading = false;
        this.error = err;
        this.registrationError = true;
        this.registrationSuccessful = false;
        this.showToastErrorMessage("Erreur lors de la modification de l'acheteur : ","Modification d'acheteur");

      });
    }
  }

  onClose() {    
    this.dialogRef.close();
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

