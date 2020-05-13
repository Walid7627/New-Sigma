import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import * as $ from 'jquery';
import { MatDialogConfig, MatDialog} from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { ProvidersReferenceComponent } from '../providers-reference/providers-reference.component';
import { ProviderDocumentsComponent } from '../provider-documents/profile-documents.component';
import { ProviderContactComponent } from '../provider-contact/provider-contact.component';


import { ProviderService } from '../service/provider.service';
import { Provider } from '../../model/provider.model';
import { DataSource } from '@angular/cdk/table';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { DialogService } from '../service/dialog-service';
import { HttpEventType } from '@angular/common/http';
import * as FileSaver from 'file-saver';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { RoleService } from '../core/role/role.service';


@Component({
  selector: 'app-all-providers',
  templateUrl: './providers-list.component.html',
  styleUrls: ['./providers-list.component.css'],
  providers: [ProviderService]
})
export class ProvidersListComponent implements OnInit {
// propriété
  allproviders: any;
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['numSiret', 'nomSociete', 'adresse', 'edit', 'delete', 'document', 'contact'];
  providers: Provider[];
  resultsLength = 0;
  searchKey: string;
  loading: boolean;

  constructor(private router: Router,private providerService: ProviderService, private dialog: MatDialog, private dialogService : DialogService, private toastrService: ToastrService, private roleService: RoleService) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(ToastContainerDirective, { static: true }) toastContainer: ToastContainerDirective;


  ngOnInit():void {
    if(!this.IamNotVisiteur())
    {
      this.displayedColumns = ['numSiret', 'nomSociete', 'adresse', 'contact'];
    }
    this.loadData();
    this.loading = false;
  }
  IamNotVisiteur() {
    return (!this.roleService.canActivate("ROLE_VISITEUR"));
  }
 
  onDelete(provider: Provider) {
    console.log("deleing");
    console.log(provider);
    this.dialogService.openConfirmDialog('Etes-vous de vouloir supprimer ce fournisseur ?')
    .afterClosed().subscribe(res =>{
      if(res){
        this.providerService.deleteProvider(provider.id).subscribe(
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

  selectRow(row) {
    console.log(row);
  }

  onCreate() {
    console.log("debut onCreate()");
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "80%";
    const dialogRef = this.dialog.open(ProvidersReferenceComponent,dialogConfig);
    dialogRef.afterClosed().subscribe(result => {
      this.loadData();
      console.log('The dialog was closed');
    });
  }

  onEdit(provider: Provider) {
    console.log("debut onEdit()");
    console.log(provider.adresse);
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "80%";
    const dialogRef = this.dialog.open(ProvidersReferenceComponent,dialogConfig);
    dialogRef.componentInstance.provider = provider;
    dialogRef.afterClosed().subscribe(result => {
      this.loadData();
      console.log('The dialog was closed');
      
    });
  }
  onEditDoc(provider: Provider) {
    console.log("debut onEdit()");
    console.log(provider.adresse);
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "80%";
    const dialogRef = this.dialog.open(ProviderDocumentsComponent,dialogConfig);
    dialogRef.componentInstance.provider = provider;
    dialogRef.afterClosed().subscribe(result => {
      this.loadData();
      console.log('The dialog was closed');
    });
  }

  onEditContact(provider: Provider) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "80%";
    const dialogRef = this.dialog.open(ProviderContactComponent,dialogConfig);
    dialogRef.componentInstance.provider = provider;
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.loadData();
    });
  }

  getAllListProviders(){
    this.providerService.getAllProviders().subscribe(allproviders => {
      this.allproviders = allproviders;
      console.log(this.allproviders);
    },
    err => {
      console.log(err);
    });

    }
    applyFilter() {
      const search = this.searchKey.trim().toLowerCase();
      this.dataSource.filter = search;
    }

    loadData() {
      this.providerService.getAllProviders()
      .subscribe(
        data => {
          this.dataSource = new MatTableDataSource(data);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        }
      );
    }

    getDocument() {
      this.loading = true;
      this.providerService.getProvidersFile().subscribe(res => {
          
          if(res.type === HttpEventType.Response) {
            let documentData: any = res;
            console.log(documentData);
            if (documentData.statusText === "OK") {
              documentData = documentData.body;
              if (documentData.type === 'application/json') {
                console.log("Vous n'avez pas les droits requis");
                this.showToastErrorMessage("Votre session doit être expirée ou vous n'avez pas les droits requis. Veuillez vous reconnecter", "Exportation de fournisseurs");
              } else {
                FileSaver.saveAs(documentData, "providers-exported");
                this.showToastSuccessMessage("Exportation réussie", "Exportation de fournisseurs");
              }
            } else {
              console.log("error");
              this.showToastErrorMessage("Votre session doit être expirée ou vous n'avez pas les droits requis. Veuillez vous reconnecter", "Exportation de fournisseurs");
            }
            
          }
  
        
        }
      );
  
      this.loading = false;
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

