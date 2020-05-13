import { Component, OnInit,ViewChild, Provider } from '@angular/core';
import { Segment } from '../../model/segment.model';
import{MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { MatDialogConfig, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {MatSort} from '@angular/material/sort';
import{ MatPaginator} from '@angular/material/paginator';
import { DialogService } from '../service/dialog-service';
import { ProviderService } from '../service/provider.service';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { from } from 'rxjs';

@Component({
  selector: 'app-providers-segment',
  templateUrl: './providers-segment.component.html',
  styleUrls: ['./providers-segment.component.css']
})
export class ProvidersSegmentComponent implements OnInit {
  segment: Segment;
  listFournisseurs: any[];
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['nomSociete', 'mail', 'cpv'];
  resultsLength = 0;
  searchKey: string;
  loading: boolean;
  constructor(private router: Router, private providerService: ProviderService, 
    public dialogRef: MatDialogRef<ProvidersSegmentComponent>,private dialog: MatDialog, private dialogService: DialogService,private toastrService: ToastrService) { }
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(ToastContainerDirective) toastContainer: ToastContainerDirective;

  ngOnInit(): void {
    this.getFournisseurBycpv();
    this.loadData();
    this.loading = false;
  }
loadData(){
  this.providerService.getProviderByCpv(this.segment.codeCPV).subscribe(
    result=> {
      if(result){
        this.listFournisseurs = JSON.parse (result['message']);
       // console.log('test de load data provider',result);
        //console.log('test de load data provider fournisseurs ',this.listFournisseurs);
        this.dataSource = new MatTableDataSource(this.listFournisseurs);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;

      }
     
      
    }
  );

}
getFournisseurBycpv(){
  this.providerService.getProviderByCpv(this.segment.codeCPV).subscribe(fournisseurs => {
    this.listFournisseurs = fournisseurs;
    console.log("list de fournisseur d'un segment",this.listFournisseurs);
  },
  err => {
    console.log(err);
  });
  }
applyFilter() {
  const search = this.searchKey.trim().toLowerCase();
  this.dataSource.filter = search;
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

onClose() {
  this.dialogRef.close();
}

}
