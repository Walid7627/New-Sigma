import { Component, OnInit,Output, EventEmitter, Inject } from '@angular/core';
import { MatDialogRef, MatDialogConfig, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ProviderService } from '../service/provider.service';
import { Observable } from 'rxjs/Observable';


@Component({
  selector: 'app-providers-logo',
  templateUrl: './providers-logo.component.html',
  styleUrls: ['./providers-logo.component.css']
})


export class ProvidersLogoComponent implements OnInit {

  nomSociete ;
  loading: boolean;
  url: String[];
  urlsTab : Observable<String[]>;

  constructor(public providerService: ProviderService, private dialog: MatDialog, public dialogRef: MatDialogRef<ProvidersLogoComponent>,@Inject(MAT_DIALOG_DATA) public data : string) { }

  ngOnInit(): void {
    this.nomSociete=this.nomSociete.replace(/\s+$/, '');
    this.nomSociete=this.nomSociete.split(' ').join('%20');
    this.urlsTab = this.providerService.getLogoo(this.nomSociete);
    this.loading=true;
    this.urlsTab.forEach(element=>{
      this.url=element;
      this.loading=false;
    })
  }
  onClose() {
    this.dialogRef.close();
  }
  onClick(src : string){
    this.data=src;
    this.dialogRef.close(this.data);
  }
  onKeyup(value: string){
    value=value.replace(/\s+$/, '');
    value=value.split(' ').join('%20');
    this.urlsTab = this.providerService.getLogoo(value);
    this.loading=true;
    this.urlsTab.forEach(element=>{
      this.url=element;
      this.loading=false;
    })
  }
}
