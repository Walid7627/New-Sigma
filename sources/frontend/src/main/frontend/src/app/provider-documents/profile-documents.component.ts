import {Component, OnInit, ViewChild} from "@angular/core";
import {AuthService} from "../core/auth/auth.service";
import { ProfileProviderService } from '../profile-provider/profile-provider.service';
import { HttpResponse, HttpEventType } from '@angular/common/http';
import { Provider } from '../../model/provider.model';
import { FileUploadService } from '../service/file-upload.service';
import { UserStorage } from '../core/userstorage/user.storage';
import * as FileSaver from 'file-saver';

import { DialogService } from '../service/dialog-service';
import { MatDialogRef } from '@angular/material/dialog';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';


import { ToastrService, ToastContainerDirective } from 'ngx-toastr';
// TODO: Suppression de document existant


export enum DocumentOpenType {
  DOWNLOAD,
  POPUP
}

@Component({
  selector: 'app-provider-documents-admin',
  templateUrl: './profile-documents.component.html',
  styleUrls: ['./profile-documents.component.css']
})
export class ProviderDocumentsComponent implements OnInit {
  provider;
    config : PerfectScrollbarConfigInterface = {};

  documents;
  // DocumentOpenType: DocumentOpenType;
  openTypeDownload: DocumentOpenType = DocumentOpenType.DOWNLOAD;
  openTypePopup: DocumentOpenType = DocumentOpenType.POPUP;

  selectedFiles: FileList
  currentFileUpload: File
  uploadProgress: { percentage: number } = { percentage: 0 };

  @ViewChild(ToastContainerDirective, { static: true }) toastContainer: ToastContainerDirective;

  constructor(private authService: AuthService,
              private profileProviderService: ProfileProviderService,
              private uploadService: FileUploadService,
              private userStorage: UserStorage,
              private toastrService: ToastrService,
		private dialogService : DialogService,
     public dialogRef: MatDialogRef<ProviderDocumentsComponent>) {

  }

  ngOnInit() {
    this.toastrService.overlayContainer = this.toastContainer;
    this.loadData();
  }
hideProgressBar(){

    console.log("loadData() currentFileUpload :"+this.currentFileUpload)
this.currentFileUpload=undefined 

	
}
  loadData() {
    
      this.profileProviderService.getByMail(this.provider.mail).subscribe(
        event => {
        if (event.type === HttpEventType.Response) {
          let data:any = event.body;
  console.log("load data  "+data.message);
          if (data.status === "OK") {
            this.provider = JSON.parse(data.message);
            this.documents=this.provider.documents;
            console.log(this.provider);
          }else{
            console.log(data.message);
          }
        }
      });
  }

  getDocument(path, name, type) {
    console.log("Get documents called with path: " + path);

    this.profileProviderService.getDocument(path).subscribe(res => {

      if(res.type === HttpEventType.Response) {
        console.log("Request result:");
        console.log(res);

        let documentData: any = res;
        documentData = documentData.body;

        if (type === this.openTypeDownload) {
          FileSaver.saveAs(documentData, name);
        } else if (type === this.openTypePopup) {
          window.open(URL.createObjectURL(documentData),'_blank');
        }
      }
    });
  }

  selectFile(event) {
    this.uploadProgress.percentage = 0;
    this.currentFileUpload = undefined;
    this.selectedFiles = event.target.files;
  }

  upload() {
    this.uploadProgress.percentage = 0;

    this.currentFileUpload = this.selectedFiles.item(0);
   
    this.uploadService.upload(this.currentFileUpload, this.provider.mail, "TYPE_DOCUMENT").subscribe(event => {

        this.selectedFiles = undefined;

        if (event.type === HttpEventType.UploadProgress) {
          this.uploadProgress.percentage = Math.round(100 * event.loaded / event.total);
        } else if (event.type === HttpEventType.Response){
          console.log("Upload done!\n");
          console.log(event);
	
          let data:any = event.body;

          if (data.status === "OK") {
            this.showToastSuccessMessage("Document ajouté avec succes","Ajout document");
            this.loadData();
          } else {

            this.showToastErrorMessage("Document existe déjà !!!","Ajout document");
            this.hideProgressBar();
          }

        }

    });
  }

   delete(path) {
    let deleted = false;
    this.dialogService.openConfirmDialog('Etes-vous sûr de vouloir supprimer ce document ?')
    .afterClosed().subscribe(res =>{
      if(res){
       this.profileProviderService.deleteDocumentByMail(path,this.provider.mail).subscribe(res => {
            console.log(res);
            deleted = true;
    })
        
      }
      console.log(deleted);
      if (deleted) {
        this.showToastSuccessMessage("Le document a été bien supprimé", "Suppression de document");
        this.loadData();
      }
    });
    
  }

  showToastErrorMessage(message, title) {
    this.toastrService.error(message, title, {
      timeOut: 3000,
    });
    window.scroll(0,0);
  }

  showToastSuccessMessage(message, title) {
    console.log("hear"); 
    this.toastrService.success(message, title, {
      timeOut: 3000,
    });
    window.scroll(0,0);
  }

 onClose() {    
    this.dialogRef.close();
  }
}
