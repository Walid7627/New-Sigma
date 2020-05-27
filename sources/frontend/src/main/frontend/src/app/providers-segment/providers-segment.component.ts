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
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-providers-segment',
  templateUrl: './providers-segment.component.html',
  styleUrls: ['./providers-segment.component.css']
})
export class ProvidersSegmentComponent implements OnInit {
  segment: Segment;
  listFournisseurs: any[];
  dataSource = new MatTableDataSource();
  codesCpv:any;
  formGroup: FormGroup;
  displayedColumns: string[] = ['nomSociete', 'mail', 'cpv'];
  resultsLength = 0;
  searchKey: string;
  loading: boolean;
  constructor(private formBuilder: FormBuilder,private router: Router, private providerService: ProviderService, 
    public dialogRef: MatDialogRef<ProvidersSegmentComponent>,private dialog: MatDialog, private dialogService: DialogService,private toastrService: ToastrService) { }
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(ToastContainerDirective) toastContainer: ToastContainerDirective;

  ngOnInit(): void {
    this.getFournisseurBycpv();
    //this.loadData();
    this.codesCpv = this.segment.codesCPV;
   console.log("tester les codes ",this.codesCpv);
    this.loading = false;
    this.createForm();
  }
  createForm(){
    this.formGroup = this.formBuilder.group({

      cpv:['',Validators.required]
    });
  }
loadData(cpv : string){
  console.log("la liste des code cpv",this.segment.codesCPV,"apres afficha");
  this.providerService.getProviderByCpv(cpv).subscribe(
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
  onSubmit(formGroup){
    console.log("on Submit avec un code cpv ",this.formGroup.value );
    const codecpv = this.formGroup.value['cpv'];
    console.log("un code cpv ",codecpv);
    this.loadData(codecpv);

  }
  /*getListCPV(){
    this.codesCpv = this.segment.codesCPV.forEach(
      item => item.libelleCpv = item.codeCpv + " - " + item.libelleCpv);
    );

  }*/
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
