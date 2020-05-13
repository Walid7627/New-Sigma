import { Component, AfterViewInit, OnInit } from '@angular/core';
import { EntityService } from "../service/entity.service";
import { Entity } from "../../model/entity.model";
import { HttpClient, HttpEventType } from '@angular/common/http';
import { EntityFormComponent } from '../entity-form/entity-form.component';
import { EntityAffectComponent } from '../entity-affect/entity-affect.component';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort,  Sort } from '@angular/material/sort';
import {  MatPaginator} from '@angular/material/paginator';
import { MatDialog, MatDialogConfig, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ViewChild } from '@angular/core';
import { catchError } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { of } from 'rxjs/observable/of';
import { DialogService } from '../service/dialog-service';
import * as FileSaver from 'file-saver';



@Component({
  selector: 'app-entities-list',
  templateUrl: './entities-list.component.html',
  styleUrls: ['./entities-list.component.css'],
  providers: [EntityService]
})



export class EntitiesListComponent implements OnInit {
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['numSiret', 'nomSociete', 'adresse', 'edit', 'delete', 'affect'];
  entities: Entity[];
  resultsLength = 0;
  searchKey: string;
  //data = Object.assign(ELEMENT_DATA);
  //dataSource = new MatTableDataSource<EntityDataSource>(this.data);
  //selection = new SelectionModel<EntityDataSource>(true, []);

  constructor(private entityService: EntityService, private dialog: MatDialog, private dialogService: DialogService) {
    //this.entities = entityService.allEntities();
  }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  /**
   * Set the sort after the view init since this component will
   * be able to query its view for the initialized sort.
   */
  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
  }
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  ngOnInit() {
    this.loadData();
  }

  onCreate() {
    console.log("debut onCreate()");
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "80%";
    const dialogRef = this.dialog.open(EntityFormComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(result => {
      this.loadData();
      console.log('The dialog was closed');
    });
  }

  onEdit(entity: Entity) {
    console.log(entity);
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "80%";
    let dialogRef = this.dialog.open(EntityFormComponent, dialogConfig);
    dialogRef.componentInstance.entity = entity;
    dialogRef.afterClosed().subscribe(result => {
      this.loadData();
      console.log('The dialog was closed');
    });
  }

  onDelete(entity: Entity) {
    console.log("deleing");
    console.log(entity);
    this.dialogService.openConfirmDialog('Etes-vous de vouloir supprimer cette entité ?')
      .afterClosed().subscribe(res => {
        if (res) {
          this.entityService.deleteEntity(entity.id).subscribe(
            res => {
              let data: any = res;
              if (data.status === "OK") {
                console.log("successfull");
                this.loadData();
              } else {

                console.log("unsuccessfull");
              }

            }, err => {
              console.log("error");
            }
          )

        }
      });
  }

  onAffect(row) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.width = "80%";
    const dialogRef = this.dialog.open(EntityAffectComponent, dialogConfig);
    dialogRef.componentInstance.entity = row;
    dialogRef.afterClosed().subscribe(result => {
      this.loadData();
      console.log('The dialog was closed');
    });
  }
  selectRow() {

  }
  onSearchClear() {
    this.searchKey = "";
    //this.applyFilter();
  }

  applyFilter() {
    const search = this.searchKey.trim().toLowerCase();
    this.dataSource.filter = search;
  }

  loadData() {
    this.entityService.getEntities()
      .pipe(catchError(() => of([])))
      .subscribe(
        data => {
          console.log("data=" + data);
          this.entities = data;
          this.dataSource = new MatTableDataSource(this.entities);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        }
      );
  }

  getDocument() {

    this.entityService.getDocument().subscribe(res => {
        
        if(res.type === HttpEventType.Response) {
          let documentData: any = res;
          console.log(documentData);
          if (documentData.statusText === "OK") {
            documentData = documentData.body;
            if (documentData.type === 'application/json') {
              console.log("Vous n'avez pas les droits requis");
            } else {
              FileSaver.saveAs(documentData, "entities-exported");
            }
          } else {
            console.log("error");
          }
          
        }

      
      }
    );


  }
}



