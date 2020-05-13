import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialogConfig, MatDialog} from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatPaginator} from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { VisiteurService } from '../service/visiteur.service';
import { Visiteur } from '../../model/visiteur.model';
import { VisiteursFormComponent } from '../visiteur-form/visiteur-form.component';
import { DialogService } from '../service/dialog-service';

@Component({
  selector: 'app-visiteurs-list',
  templateUrl: './visiteurs-list.component.html',
  styleUrls: ['./visiteurs-list.component.css']
})
export class VisiteursListComponent implements OnInit {
  // propriété
  allvisiteur: any;
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['nom', 'prenom', 'entite', 'edit', 'delete'];
  resultsLength = 0;
  searchKey: string;

  constructor(private router: Router, private visiteurService: VisiteurService, private dialog: MatDialog, private dialogService: DialogService) { }

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
    const dialogRef = this.dialog.open(VisiteursFormComponent, dialogConfig);
    dialogRef.afterClosed().subscribe(result => {
      this.loadData();
      console.log('The dialog was closed');
    });
  }

  
  onEdit(visiteur: Visiteur) {
    console.log(visiteur.adresse);
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "80%";
    let dialogRef = this.dialog.open(VisiteursFormComponent, dialogConfig);
    dialogRef.componentInstance.visiteur = visiteur;
    dialogRef.afterClosed().subscribe(result => {
      this.loadData();
      console.log('The dialog was closed');
    });
  }

  onDelete(visiteur: Visiteur) {

    this.dialogService.openConfirmDialog('Etes-vous sûr de vouloir supprimer ce visiteur  ?')
      .afterClosed().subscribe(res => {
        if (res) {
          this.visiteurService.deleteVisiteur(visiteur.id).subscribe(
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
    this.visiteurService.getAllVisiteurs()
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



