import {Component, OnInit} from '@angular/core';
import {MatDialog, MatDialogConfig, MatDialogRef} from '@angular/material/dialog';
import {Purchaser} from '../../model/purchaser.model';
import {Address} from '../../model/address.model';
import {PurchaserService} from '../service/purchaser.service';
import {CodeAPEService} from '../service/code-ape.service';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {Router} from "@angular/router";
import {CompanyType, Provider} from "../../model/provider.model";
//import { formArrayNameProvider } from '@angular/forms/src/directives/reactive_directives/form_group_name';
import { $$ } from 'protractor';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { ToastrService, ToastContainerDirective } from 'ngx-toastr';
import {Qualification} from "../../model/qualification.model";
import {ProviderService} from "../service/provider.service";
import {HttpEventType} from "@angular/common/http";
import {map} from "rxjs/operators";
import {ProviderQualificationComponent} from "../provider-qualification/provider-qualification.component";
import {MyBarChartComponent} from "../my-bar-chart/my-bar-chart.component";

@Component({
  selector: 'app-purchaser-form',
  templateUrl: './provider-show-qualification.component.html',
  styleUrls: ['./provider-show-qualification.component.css'],
})

export class ProviderShowQualificationComponent implements OnInit {

  provider;

  qualification: Qualification = new Qualification();
  listCodeApe: any;

  registrationSuccessful: boolean = false;
  registrationError: boolean = false;
  error: string;
  loading: boolean;
  config : PerfectScrollbarConfigInterface = {};
  qualificationForm: FormGroup;

  listProviders: any;
  private k: number;
  private a: number;

  donnes_a_afficher: any;

  liste_frs: Array<number>;
  private fournisseurs_nb: any;


  constructor(private dialog: MatDialog, private providerService:ProviderService, private fb: FormBuilder, public dialogRef: MatDialogRef<ProviderShowQualificationComponent>,private toastrService: ToastrService) { }

  ngOnInit() {
    this.loading = false;



    this.qualificationForm = this.fb.group({
      fournisseurs: ['', Validators.required],
    });

    this.listProviders = this.providerService.getQualifiedProviders().pipe(map(result => {
      const items = <any[]>result;
      items.forEach(item => item.libelleProviders = item.nomSociete);
      return items;
    }));

  }

  onSubmit({value, valid}: {value: Qualification, valid: boolean}) {

    // @ts-ignore
    this.fournisseurs_nb = value.fournisseurs;

    this.error = "";
    this.registrationError = false;
    this.registrationSuccessful = false;
    this.loading = true;
    console.log("avant submit");

    this.liste_frs = new Array<number>();
    this.k = 0;
    while (this.k < this.fournisseurs_nb.length) {

      this.liste_frs.push(this.fournisseurs_nb[this.k]);

      this.k = this.k + 1;
    }
    this.providerService.getQualifications(this.liste_frs).subscribe(
      event => {
        if (event.type === HttpEventType.Response) {
          let data:any = event.body;

          if (data.status === "OK") {
            this.donnes_a_afficher = JSON.parse(data.message);
            this.showGraphique(this.donnes_a_afficher);
          } else {
            this.error = data.message;
            this.registrationError = true
            this.showToastErrorMessage("Erreur lors de la récupération d'informations : " + this.error, "Récupération qualification");
          }
        }
      });
  }

  showGraphique(donnees) {
    console.log("debut showGraphique()");
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "80%";
    const dialogRef = this.dialog.open(MyBarChartComponent,dialogConfig);
    dialogRef.componentInstance.donnees = donnees;

      dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.ngOnInit();
    });
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

