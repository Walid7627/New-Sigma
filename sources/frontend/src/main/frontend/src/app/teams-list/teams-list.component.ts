import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialogConfig, MatDialog } from '@angular/material/dialog';
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import { DialogService } from '../service/dialog-service';
import { TeamService } from '../service/team.service';
import {PurchaserFormComponent} from "../purchaser-form/purchaser-form.component";
import {TeamFormComponent} from "../teams-form/team-form.component";
import {HttpEventType} from "@angular/common/http";
import {RoleService} from "../core/role/role.service";
import {EntityService} from "../service/entity.service";
import {Provider} from "../../model/provider.model";
import {ProviderContactComponent} from "../provider-contact/provider-contact.component";
import {Team} from "../../model/team.model";
import {TeamPurchaserComponent} from "../teams-purchaser/team-purchaser.component";


@Component({
  selector: 'app-all-teams',
  templateUrl: './teams-list.component.html',
  styleUrls: ['./teams-list.component.css'],
  providers: [TeamService, RoleService, EntityService]
})
export class TeamListComponent implements OnInit {
  // propriété
  alladmins: any;
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['libelle', 'responsable', 'entite', 'edit', 'delete', 'purchasers'];
  resultsLength = 0;
  searchKey: string;

  constructor(private router: Router, private teamService: TeamService, private dialog: MatDialog, private dialogService: DialogService, private roleService: RoleService) { }

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  ngOnInit(): void {
    this.loadData();
  }

  onCreate() {
    console.log("debut onCreate()");
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "80%";
    const dialogRef = this.dialog.open(TeamFormComponent, dialogConfig);
    dialogRef.afterClosed().subscribe(result => {
      this.loadData();
      console.log('The dialog was closed');
    });
  }

  onEdit(team) {
    console.log(team.libelle);
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "80%";
    let dialogRef = this.dialog.open(TeamFormComponent, dialogConfig);
    dialogRef.componentInstance.team = team;
    dialogRef.afterClosed().subscribe(result => {
      this.loadData();
      console.log('The dialog was closed');
    });
  }

  onDelete(team) {
    this.dialogService.openConfirmDialog('Etes-vous sûr de vouloir supprimer cette equipe ?')
      .afterClosed().subscribe(res => {
      if (res) {
        this.teamService.deleteAdmin(team.id).subscribe(
          res => {
            let data: any = res;
            if (data.status === "OK") {
              console.log("successfull");
              this.loadData();
            } else {
              console.log("unsuccessfull ." + data.message);
            }
          }, err => {
            console.log("error");
          }
        )
      }
    });
  }

  onDisplay() {

  }

  onSearchClear() {
    this.searchKey = "";
  }
  applyFilter() {
    const search = this.searchKey.trim().toLowerCase();
    this.dataSource.filter = search;
  }

  loadData() {
    if (this.roleService.getRole() === "ROLE_ADMINISTRATEUR_ENTITE" || this.roleService.getRole() === "ROLE_ADMINISTRATEUR_SIGMA") {
      this.teamService.getAllTeam()
        .subscribe(
          data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;
          }
        );
    }
  }

  onEditPurchaser(team: Team) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "80%";
    const dialogRef = this.dialog.open(TeamPurchaserComponent,dialogConfig);
    dialogRef.componentInstance.team = team;
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.loadData();
    });
  }

  selectRow(row) {

  }
}


