import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialogConfig, MatDialog} from '@angular/material/dialog';
import {MatSort} from '@angular/material/sort';
import{ MatPaginator} from '@angular/material/paginator';
import{MatTableDataSource } from '@angular/material/table';
import { DialogService } from '../service/dialog-service';
import { SegmentService } from '../service/segment.service';
import { ProvidersReferenceComponent } from '../providers-reference/providers-reference.component';
import { Segment } from '../../model/segment.model';
import { SegmentComponent } from '../segment/segment.component';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { from } from 'rxjs';
import { ProvidersSegmentComponent } from '../providers-segment/providers-segment.component';


@Component({
  selector: 'app-all-segments',
  templateUrl: './segments-list.component.html',
  styleUrls: ['./segments-list.component.css'],
  providers: [SegmentService]
})
export class SegmentsListComponent implements OnInit {
  // propriété
  allaSegments: any;
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['nom', 'cpv', 'acheteur', 'edit', 'delete','fournisseur'];
  resultsLength = 0;
  searchKey: string;
  segments: Segment[];
  loading: boolean;

  constructor(private router: Router, private segmentService: SegmentService, private dialog: MatDialog, private dialogService: DialogService,private toastrService: ToastrService) { }

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(ToastContainerDirective) toastContainer: ToastContainerDirective;

  ngOnInit(): void {
    this.loadData();
    this.loading = false;
  }

  onCreate() {
    console.log("debut onCreate() segmentsListComponent ");
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "80%";
    const dialogRef = this.dialog.open(SegmentComponent,dialogConfig);
    dialogRef.afterClosed().subscribe(result => {
      this.loadData();
      console.log('The dialog was closed');
    });
  }

onEdit(segment: Segment) {
      console.log("debut onEdit()");
      console.log(segment.libelle);
      const dialogConfig = new MatDialogConfig();
      dialogConfig.disableClose = true;
      dialogConfig.autoFocus = true;
      dialogConfig.width = "80%";
      const dialogRef = this.dialog.open(SegmentComponent,dialogConfig);
      dialogRef.componentInstance.segment = segment;
     // console.log('Test de editer seegment ',segment.codeAPE);
      dialogRef.afterClosed().subscribe(result => {
        this.loadData();
        console.log('The dialog was closed');
      });
  }
  

  onDelete(segment: Segment) {
    console.log("deleing");
    console.log('TEST DE ID SEGMENT ',segment.id);
    this.dialogService.openConfirmDialog('Etes-vous de vouloir supprimer ce segment ?')
    .afterClosed().subscribe(res =>{
      if(res){
        this.segmentService.deleteSegment(segment.id).subscribe(
          res=> {
            let data:any = res;
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
  onEditFour(segment){

  }

  onDisplay() {

  }
  getAllListSegments(){
    this.segmentService.getAllSegment().subscribe(allsegments => {
      this.allaSegments = allsegments;
      console.log(this.allaSegments);
    },
    err => {
      console.log(err);
    });
    }

 
  applyFilter() {
    const search = this.searchKey.trim().toLowerCase();
    this.dataSource.filter = search;
  }
  getListFournisseur(segment: Segment) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "80%";
    const dialogRef = this.dialog.open(ProvidersSegmentComponent,dialogConfig);
    dialogRef.componentInstance.segment = segment;
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.loadData();
    });
  }

  loadData() {
    this.segmentService.getAllSegment()
    .subscribe(
      data => {
        console.log('test de load data',data);
        this.dataSource= new MatTableDataSource(data);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      }
    );
  }

  selectRow(row) {

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
}


