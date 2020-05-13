import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialogConfig, MatDialog } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { DialogService } from '../service/dialog-service';
import { PurchaserService } from '../service/purchaser.service';
import { PurchaserFormComponent } from '../purchaser-form/purchaser-form.component';
import { RoleService } from '../core/role/role.service';
import { EntityService } from '../service/entity.service';
import { HttpEventType } from '@angular/common/http';


@Component({
  selector: 'app-all-purchasers',
  templateUrl: './purchaser-list.component.html',
  styleUrls: ['./purchaser-list.component.css'],
  providers: [PurchaserService, EntityService]
})
export class PurchaserListComponent implements OnInit {
  // propriété
  alladmins: any;
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['nom', 'prenom', 'entite','team', 'edit', 'delete'];
  resultsLength = 0;
  searchKey: string;

  constructor(private router: Router, private purchaserService: PurchaserService, private dialog: MatDialog, private dialogService: DialogService, private roleService: RoleService, private entityService: EntityService) { }

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
    const dialogRef = this.dialog.open(PurchaserFormComponent, dialogConfig);
    dialogRef.afterClosed().subscribe(result => {
      this.loadData();
      console.log('The dialog was closed');
    });
   
  }

  onEdit(purchaser) {
    console.log(purchaser.adresse);
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "80%";
    let dialogRef = this.dialog.open(PurchaserFormComponent, dialogConfig);
    dialogRef.componentInstance.purchaser = purchaser;
    dialogRef.afterClosed().subscribe(result => {
      this.loadData();
      console.log('The dialog was closed');
    });
  }

  onDelete(purchaser) {
    this.dialogService.openConfirmDialog('Etes-vous sûr de vouloir supprimer cet acheteur ?')
    .afterClosed().subscribe(res => {
      if (res) {
        this.purchaserService.deleteAdmin(purchaser.id).subscribe(
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
    if (this.roleService.getRole() === "ROLE_ADMINISTRATEUR_ENTITE" ) {
      this.entityService.getMembers()

      .subscribe(
        event => {
          if (event.type === HttpEventType.Response) {
            let data:any = event.body;
  
            if (data.status === "OK") {
              data = JSON.parse(data.message);
  
              this.dataSource = new MatTableDataSource(data);
              this.dataSource.paginator = this.paginator;
              this.dataSource.sort = this.sort;
  
            } else {
              console.log("Error while loading purchaser");
              console.log(data);
            }
          }
  
        },
  
        err => {
          console.log("Error while loading purchaser");
          console.log(err);
        }
        
      );
    }
  }

  selectRow(row) {

  }
}


