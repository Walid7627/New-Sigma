import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialogConfig, MatDialog} from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { AdminEntityService } from '../service/admin-entity.service';
import { AdminEntity } from '../../model/admin-entity.model';
import { AdminsEntityFormComponent } from '../admin-entity-form/admin-entity-form.component';
import { DialogService } from '../service/dialog-service';


@Component({
  selector: 'app-all-admins-entity',
  templateUrl: './admins-entity-list.component.html',
  styleUrls: ['./admins-entity-list.component.css'],
  providers: [AdminEntityService]
})
export class AdminsEntityListComponent implements OnInit {
  // propriété
  alladmins: any;
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['nom', 'prenom', 'entite', 'edit', 'delete'];
  resultsLength = 0;
  searchKey: string;

  constructor(private router: Router, private adminService: AdminEntityService, private dialog: MatDialog, private dialogService: DialogService) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  ngOnInit(): void {
    this.loadData();
  }

  onCreate() {
    console.log("debut onCreate()");
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "80%";
    const dialogRef = this.dialog.open(AdminsEntityFormComponent, dialogConfig);
    dialogRef.afterClosed().subscribe(result => {
      this.loadData();
      console.log('The dialog was closed');
    });
  }

  
  onEdit(admin: AdminEntity) {
    console.log(admin.adresse);
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "80%";
    let dialogRef = this.dialog.open(AdminsEntityFormComponent, dialogConfig);
    dialogRef.componentInstance.admin = admin;
    dialogRef.afterClosed().subscribe(result => {
      this.loadData();
      console.log('The dialog was closed');
    });
  }

  onDelete(admin: AdminEntity) {

    this.dialogService.openConfirmDialog('Etes-vous sûr de vouloir supprimer cette administrateur d\'entité ?')
      .afterClosed().subscribe(res => {
        if (res) {
          this.adminService.deleteAdmin(admin.id).subscribe(
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
    this.adminService.getAllAdmins()
      .subscribe(
        data => { 
          this.dataSource = new MatTableDataSource(data);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        }
      );
  }

  selectRow(row) {

  }
}


