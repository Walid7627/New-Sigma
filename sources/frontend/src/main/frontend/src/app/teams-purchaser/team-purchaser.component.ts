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
import {TeamService} from "../service/team.service";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {map} from "rxjs/operators";
import {PurchaserService} from "../service/purchaser.service";
import {Team} from "../../model/team.model";


@Component({
  selector: 'app-team-purchaser',
  templateUrl: './team-purchaser.component.html',
  styleUrls: ['./team-purchaser.component.css']
})
export class TeamPurchaserComponent implements OnInit {

  dataSource = new MatTableDataSource();
  team: Team = new Team();
  config : PerfectScrollbarConfigInterface = {};

  user: FormGroup;
  listPurchasers: any;
  error: string;
  loading: boolean;
  registrationSuccessful: boolean = false;
  registrationError: boolean = false;


  @ViewChild(ToastContainerDirective) toastContainer: ToastContainerDirective;

  constructor(private toastrService: ToastrService,  private fb: FormBuilder, public teamService: TeamService, public serviceAcheteur: PurchaserService,
              private dialogService : DialogService, public dialogRef: MatDialogRef<TeamPurchaserComponent>) {
  }

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;


  ngOnInit() {
    
    this.toastrService.overlayContainer = this.toastContainer;
    this.user = this.fb.group({
      responsable: [null, Validators.required]
    });

    this.listPurchasers = this.serviceAcheteur.getFreePurchaser().pipe(map(result => {
      const items = <any[]>result;
      items.forEach(item => item.libellePurchasers = item.nom + " - " + item.prenom);
      return items;
    }));
  }

  loadData() {

    this.teamService.getById(this.team.id).subscribe(
      event => {
        if (event.type === HttpEventType.Response) {
          let data:any = event.body;

          if (data.status === "OK") {
            this.team = JSON.parse(data.message);
            console.log(this.team);
          }
        }
      });

    this.listPurchasers = this.serviceAcheteur.getFreePurchaser().pipe(map(result => {
      const items = <any[]>result;
      items.forEach(item => item.libellePurchasers = item.nom + " - " + item.prenom);
      return items;
    }));
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

  onSubmit({value}: {value: Team}) {

    console.log(value.responsable);
    this.teamService.addPurchaser(value.responsable, this.team.id)
      .subscribe(response => {
        if (response.type === HttpEventType.Response) {
          let data:any = response.body;
          console.log(data);
          if (data.status === "OK") {
            this.loadData();
            this.showToastSuccessMessage("Acheteur ajouté avec succès", "Ajout d'un acheteur");
            this.user.reset();
          } else {
            console.log("Error while adding new contact");
            this.showToastErrorMessage("Acheteur non ajouté", "Ajout d'un acheteur");
          }
        }
      }, error => {
        console.log('Error: ');
        console.log(error);
        this.showToastErrorMessage("Cet acheteur ne correspond pas à cette équipe", "Suppression d'un acheteur");
      });
  }



  onClose() {
    this.dialogRef.close();
  }


  removePurchaser(acheteur) {
    console.log("Removing:", acheteur);

    this.dialogService.openConfirmDialog('Etes-vous sûr de vouloir supprimer cet acheteur ?')
      .afterClosed().subscribe(res =>{
      if(res){
        this.teamService.removePurchaser(acheteur.id, this.team.id).subscribe(
          response => {
            console.log("Remove purchaser response", response);

            if (response.type === HttpEventType.Response) {
              let data:any = response.body;
              if (data.status === "OK") {
                this.loadData();
                this.showToastSuccessMessage("Acheteur supprimé de l'équipe avec succès", "Suppression de l'acheteur");
                console.log("Purchaser removed successfully");
              } else {
                console.log("Error when removing purchaser", data.message);
                this.showToastErrorMessage("Cet acheteur ne corresponds pas à cette équipe", "Suppression de l'acheteur");
              }
            }
          },
          err => {
            console.log("Error when removing purchaser", err);
            this.showToastErrorMessage("Cet acheteur ne corresponds pas à cette équipe", "Suppression de l'acheteur");
          }
        )
      }
    });
  }

}
