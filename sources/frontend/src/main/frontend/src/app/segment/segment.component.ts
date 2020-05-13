import { Component, OnInit, ViewChild, Provider } from '@angular/core';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { SegmentService } from '../service/segment.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';
import { APE } from '../../model/ape.model';
import { CPV } from '../../model/cpv.model';
import { CodeAPEService } from '../service/code-ape.service';
import { CodeCPVService } from '../service/code-cpv.service';
import { map } from 'rxjs/operators';
import { Segment } from '../../model/segment.model';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { Purchaser } from '../../model/purchaser.model';
import { PurchaserService } from '../service/purchaser.service';

@Component({
  selector: 'app-segment',
  templateUrl: './segment.component.html',
  styleUrls: ['./segment.component.css']
})
export class SegmentComponent implements OnInit {
  config: PerfectScrollbarConfigInterface = {};
  loading: boolean;
  codeape: APE;
  codecpv: CPV;
  acheteur: Purchaser;
  formGroup: FormGroup;
  listAcheteur: any;
  listcpv: any;
  nom: string;
  action: string;
  segment: Segment = new Segment(null,null,null);
  error: string;
  registrationError: boolean =false;
  registrationSuccessful: boolean = false;

  @ViewChild(ToastContainerDirective) toastContainer: ToastContainerDirective;

  constructor(private toastrService: ToastrService,private servicePurchaser: PurchaserService, private serviceAPE : CodeAPEService, private serviceCPV: CodeCPVService,private segmentService: SegmentService, private formBuilder: FormBuilder,private dialog: MatDialog, public dialogRef: MatDialogRef<SegmentComponent>) { }

  ngOnInit() {
   console.log("le segment");
   this.toastrService.overlayContainer = this.toastContainer;
    this.loading = false;
    console.log("le segment");
    //this.getListAPE();
    this.getListAcheteur();
    this.getListCPV();
    this.createForm();
    console.log("test 1 le segment");
    //console.table('lite de code cpv ',this.listcpv[0]);
    this.action = "L'inscription";
    if(this.segment.id ) {
      this.action= "La modification";
      this.formGroup.setValue({
        nom: this.segment.libelle,
        acheteur: this.segment.acheteur,
        cpv: this.segment.codeCPV
      });
    }
  }
  createForm(){
    this.formGroup = this.formBuilder.group({
      acheteur: ['', Validators.required],
      cpv:['',Validators.required],
      nom:['',Validators.required]
    });
  }
  onClose() {
    this.dialogRef.close();
  }
 /* getListAcheteur(){
    this.listape = this.serviceAPE.getCodeApe().pipe(map(result => {
      const items = <any[]>result;
      items.forEach(item => item.libelleApe = item.codeApe + " - " + item.libelleApe);
      console.log('itemes ',items);
      return items;
    }));

    
  }*/
  getListAcheteur(){
    this.listAcheteur=this.servicePurchaser.getAllPurchaser().pipe(map(
      result=>{
        const items =<any[]>result;
        items.forEach(item => item.libelleAcheteur =item.nom+"-"+item.prenom);
        console.log("itemes " , items);
        return items;
      }))
  }
  getListCPV(){
    this.listcpv = this.serviceCPV.getCodeCpv().pipe(map(result => {
      const items = <any[]>result;
      items.forEach(item => item.libelleCpv = item.codeCpv + " - " + item.libelleCpv);
      return items;
    }));

  }
  onSubmit({value, valid}: {value: Segment, valid: boolean}){

    console.log('TEST Value ', value.libelle, value.codeCPV);
    this.error = "";
    this.registrationError = false;
    this.registrationSuccessful = false;
    this.loading = true;
    console.log("avant submit");
    
   
    if (this.segment.id == null ) {
      const seg = this.formGroup.value; 
      const s = new Segment(seg['nom'], seg['cpv'], seg['purchaser']);
      this.segment =s ;
      this.segmentService.save(s)
      .subscribe(res => {
        this.loading = false;
        console.log("Service data:");
        console.log(res);
        let data:any = res;
        if (data.status === "OK") {
          this.registrationSuccessful = true;
          this.showToastSuccessMessage("Le segment a été ajouté avec succès","Ajout du segment");
         // this.scrollToSuccessMessage();
          this.formGroup.reset();
        } else {
          this.error = data.message;
          this.registrationError = true
          // this.scrollToErrorMessage();
          this.showToastErrorMessage("Erreur lors de l'ajout du segment : "+this.error,"Ajout d'un segment");
        }
        console.log("Data:");
        console.log(data);
      }, err => {
        this.loading = false;
        this.error = err;
        this.registrationError = true;
        this.registrationSuccessful = false;
        // this.scrollToErrorMessage();
        this.showToastErrorMessage("Erreur lors de l'ajout du segment: "+this.error,"Ajout d'un segment");
      });


  }
  else{
    value.id = this.segment.id;
  const formValue = this.formGroup.value;
  this.segment.libelle = formValue['nom'];
  this.segment.codeCPV = formValue['cpv'];
  this.segment.acheteur = formValue['purchaser'];
  console.log(this.segment.libelle);
      this.segmentService.updateSegment(this.segment)
      .subscribe(res => {
        this.loading = false;
        console.log("segmentService data edited:");
        console.log(res);
        let data:any = res;
        if (data.status === "OK") {
          this.registrationSuccessful = true;
          // this.scrollToSuccessMessage();
          this.showToastSuccessMessage("Le segment a été modifier avec succès","Modification d'un segment");

        } else {
          this.error = data.message;
          this.registrationError = true
          // this.scrollToErrorMessage();
          this.showToastErrorMessage("Erreur lors de la modification d'un segment : ","Modification de segment");

        }
        console.log("Data:");
        console.log(data);
      }, err => {
        this.loading = false;
        this.error = err;
        this.registrationError = true;
        this.registrationSuccessful = false;
        this.showToastErrorMessage("Erreur lors de la modification d'un segment: ","Modification dd'un segment");

      });
  }

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
  window.scroll(0, 0);
}

}
