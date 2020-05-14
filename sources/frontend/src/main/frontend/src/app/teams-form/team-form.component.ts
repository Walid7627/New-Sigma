import {Component, OnInit, ViewChild} from '@angular/core';
import {MatDialogRef, MatDialogConfig, MatDialog} from '@angular/material/dialog';
import {Team} from '../../model/team.model';
import { TeamService } from '../service/team.service';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { ToastrService, ToastContainerDirective } from 'ngx-toastr';
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {map} from "rxjs/operators";
import {PurchaserService} from "../service/purchaser.service";
import {EntityService} from "../service/entity.service";
import {validate} from "codelyzer/walkerFactory/walkerFn";
import {AuthService} from "../core/auth/auth.service";
import {AdminEntityService} from "../service/admin-entity.service";

@Component({
  selector: 'app-team-form',
  templateUrl: './team-form.component.html',
  styleUrls: ['./team-form.component.css'],
  providers:[TeamService, EntityService, PurchaserService]
})

export class TeamFormComponent implements OnInit {

  team: Team = new Team();
  dataSource = new MatTableDataSource();
  registrationSuccessful: boolean = false;
  registrationError: boolean = false;
  teamForm: FormGroup;
  error: string;
  loading: boolean;
  config : PerfectScrollbarConfigInterface = {};
  listResp: any;
  listEntite: any;
  listPurchasers: any;
  private action: string;
  entite: any;

  constructor(private teamService:TeamService, private fb: FormBuilder, public dialogRef: MatDialogRef<TeamFormComponent>,private toastrService: ToastrService,
              public serviceAcheteur: PurchaserService, public adminEntityService: AdminEntityService, public serviceEntite: EntityService, public authService: AuthService) { }

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngOnInit() {
    this.loading = false;
    this.loadData();
  }

  loadData() {
    this.teamService.getAllTeam()
      .subscribe(
        data => {
          this.dataSource = new MatTableDataSource(data);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        }
      );

    if (this.team.id) {
      this.teamForm = this.fb.group({
        libelle: [''],
        responsable: [null],
        //entite: [null]
      });
    } else {
      this.teamForm = this.fb.group({
        libelle: ['', Validators.required],
        responsable: [null, Validators.required],
        //entite: [null, Validators.required],
        membres: [null]
      });
    }

    this.action = "L'inscription";
    if (this.team.id) {
      this.teamForm.setValue({
        libelle: this.team.libelle,
        responsable: this.team.responsable,
        //entite: this.team.entite
      });
    }
    this.listResp = this.serviceAcheteur.getFreePurchaser().pipe(map(result => {
      const items = <any[]>result;
      items.forEach(item => item.libelleResp = item.nom + "  " + item.prenom);
      return items;
    }));

    /*
    this.listEntite = this.serviceEntite.getEntities().pipe(map(result => {
      const items = <any[]>result;
      items.forEach(item => item.libelleEntite = item.nomSociete + " - " + item.numSiret);
      return items;
    }));

     */

    this.listPurchasers = this.serviceAcheteur.getFreePurchaser().pipe(map(result => {
      const items = <any[]>result;
      items.forEach(item => item.libellePurchasers = item.nom + "  " + item.prenom);
      return items;
    }));

    console.log(this.authService.getCurrentUser().id);
  }

  onSubmit({value}: {value: Team}) {
    //console.log("auth--"+this.authService.getCurrentUser().id);

    console.log(value);
    this.error = "";
    this.registrationError = false;
    this.registrationSuccessful = false;
    this.loading = true;
    console.log("avant submit");
    if (this.team.id == null) {

      this.teamService.add(value)
        .subscribe(res => {
          this.loading = false;
          console.log("Service data:");
          let data: any = res;
          if (data.status === "OK") {
            this.registrationSuccessful = true;
            this.showToastSuccessMessage("L'équipe d'achats a été ajoutée avec succès", "Ajout d'équipe d'achats");
            // this.scrollToSuccessMessage();
            this.teamForm.reset();
          } else {
            this.error = data.message;
            this.registrationError = true
            // this.scrollToErrorMessage();
            this.showToastErrorMessage("Erreur lors de l'ajout de l'équipe d'achats : " + this.error, "Ajout d'équipe d'achats");
          }
          console.log("Data:");
        }, err => {
          this.loading = false;
          this.error = err;
          this.registrationError = true;
          this.registrationSuccessful = false;
          // this.scrollToErrorMessage();
          this.showToastErrorMessage("Erreur lors de l'ajout de l'équipe d'achats : " + this.error, "Ajout d'équipe d'achats");
        });
    } else {
      // @ts-ignore
      if ((value.libelle != "" && value.libelle != this.team.libelle) || (value.responsable.id != this.team.responsable.id)) {
        console.log("modifyinggggg:");
        value.id = this.team.id;

        if (value.libelle == "") {
          value.libelle = this.team.libelle;
        }

        // @ts-ignore
        if (value.responsable.id == this.team.responsable.id) {
          value.responsable = null;
          
        }

        if (value.entite == null) {
          value.entite = this.team.entite;
        }

        this.teamService.update(value, this.team.id)
          .subscribe(res => {
            this.loading = false;
            console.log("teamService data edited:");
            console.log(res);
            let data: any = res;
            if (data.status === "OK") {
              this.registrationSuccessful = true;
              // this.scrollToSuccessMessage();
              this.showToastSuccessMessage("L'équipe d'achats a été modifiée avec succès", "Modification de l'équipe d'achats");

            } else {
              this.error = data.message;
              this.registrationError = true
              // this.scrollToErrorMessage();
              this.showToastErrorMessage("Erreur lors de la modification de l'équipe d'achats : ", "Modification de l'équipe d'achats");

            }
            console.log("Data:");
            console.log(data);
          }, err => {
            this.loading = false;
            this.error = err;
            this.registrationError = true;
            this.registrationSuccessful = false;
            this.showToastErrorMessage("Erreur lors de la modification de l'équipe d'achats : ", "Modification de l'équipe d'achats");

          });
      } else {
        this.loading = false;
        this.showToastErrorMessage("Veuillez entrer les nouvelles données", "Modification de l'équipe d'achats");
      }
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

