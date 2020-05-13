import { Component, OnInit, ViewChild } from '@angular/core';
import { AuthService } from '../core/auth/auth.service';

import { ActivatedRoute } from '@angular/router';
import { UserService } from '../service/user.service';
import { ToastrService, ToastContainerDirective } from 'ngx-toastr';

import * as $ from 'jquery';
import { ProviderService } from '../service/provider.service';
import { HttpEventType } from '@angular/common/http';
import { CodeAPEService } from '../service/code-ape.service';

declare var $ :any;

function jqueryHandler() {
  $(document).ready(function() {
    console.log( "ready!" );
    (<any>$('.carousel')).carousel();
  });
  
}


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  token: string;
  providers: any[];
  count: number = 0;
  imagesUrl = new Map();
  defaultImage = 'assets/files/defaultImage.png';
  @ViewChild(ToastContainerDirective, { static: true }) toastContainer: ToastContainerDirective;
  constructor(private authService: AuthService, private route: ActivatedRoute, private userSercice: UserService, private toastrService: ToastrService, private providerService: ProviderService, private codeAPEService : CodeAPEService) { }

  ngOnInit() {
    this.toastrService.overlayContainer = this.toastContainer;
    this.token = this.route.snapshot.paramMap.get("token");
    if (this.token) {
      this.activate();
    }

    this.loadData();
    jqueryHandler();
  }

 

  activate() {
    this.userSercice.activateUser(this.token).subscribe(
      res => {
        let data: any = res;
        if (data.status === "OK") {
          this.showToastSuccessMessage("Votre compte a été bien activé. Veuillez vous connecter !", "Activation fournisseur");
        } else {
          this.showToastErrorMessage("Erreur lors de l'activation. " + data.message, "Activation fournisseur");
        }
      }, err => {
        this.showToastErrorMessage("Erreur lors de l'activation.", "Activation fournisseur");
      });
  }

  showToastErrorMessage(message, title) {
    this.toastrService.error(message, title, {
      timeOut: 10000,
    });
    window.scroll(0, 0);
  }

  showToastSuccessMessage(message, title) {
    this.toastrService.success(message, title, {
      timeOut: 10000,
    });
    window.scroll(0, 0);
  }

  createImageFromBlob(id: number, image: Blob) {
    if (image.type.startsWith("image")) {
      let reader = new FileReader();
      reader.addEventListener("load", () => {
        this.imagesUrl.set(id, reader.result);
      }, false);
      if (image) {
        reader.readAsDataURL(image);
      }
    }
  }

  loadData() {
    this.providerService.getLastProviders()
      .subscribe(
        data => {
          this.providers = data;
          for (let i = 0; i < this.providers.length; i++) {
            if(this.providers[i].logo) {
              this.providerService.getLogo(this.providers[i].logo).subscribe(res => {
    
                if (res.type === HttpEventType.Response) {
                  let documentData: any = res;
                  documentData = documentData.body;
                
                  this.createImageFromBlob(this.providers[i].id, documentData);
                } 
              });
            } 
            this.codeAPEService.getCodeApeLibelle(this.providers[i].codeAPE).subscribe(
              data => {
                this.providers[i].domaine = data.libelleApe;
              });
          }
        }
      );
      
        
  }

  

}
