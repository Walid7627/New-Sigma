import { Component, OnInit, ViewChild } from '@angular/core';
import { AdminEntityService } from '../service/admin-entity.service';
import { Entity } from '../../model/entity.model';
import { AdminEntity } from '../../model/admin-entity.model';
import { EntityService } from '../service/entity.service';
import { HttpEventType } from '@angular/common/http';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { MatDialogRef } from '@angular/material/dialog';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-entity-affect',
  templateUrl: './entity-affect.component.html',
  styleUrls: ['./entity-affect.component.css'],
  providers: [EntityService]
})
export class EntityAffectComponent implements OnInit {

  listFreeAdmin: any;
  entity: Entity;
  adminForm: FormGroup;
  @ViewChild(ToastContainerDirective, { static: true }) toastContainer: ToastContainerDirective;

  constructor(private toastrService: ToastrService, public dialogRef: MatDialogRef<EntityAffectComponent>, public AEService:AdminEntityService, public entityService:EntityService, private fb: FormBuilder) { }

  ngOnInit() {
    this.toastrService.overlayContainer = this.toastContainer;
    this.listFreeAdmin = this.AEService.getFreeAdmins().pipe(map(result => {

      const items =<any[]>result;
      if (this.entity.administrateur) {
        items.push(this.entity.administrateur);
      }
      //items.push(result[0].prenom);
      items.forEach(item => item.administrateurName = item.nom + " - " + item.prenom + "   (" + item.mail + ")");
      console.log(items[0].administrateurName);
      return items;
    }));
    let id = null;
    if (this.entity.administrateur) {
      id = this.entity.administrateur.id;
    }
    this.adminForm = this.fb.group({
      administrateur: [id ,[Validators.required]]
    });
    
  }

  onSubmit({value, valid}: {value: any, valid: boolean}) {
    console.log("affect");
    this.entityService.affectAdminToEntity(this.entity, value.administrateur).subscribe(
      res => {
        let data:any = res;
        if (data.status === "OK") {
          console.log("data status: ok");
          this.showToastSuccessMessage("Affectation d'administrateur", "L'administrateur a été bien affecté à l'entité");
        } else {
          console.log("data status: ko");
          this.showToastErrorMessage("Affectation d'administrateur", "Erreur lors de l'affectation : " + data.message);
        }
      }, err => {
        console.log("request error");
      }
    );
  }

  onClose() {
    this.dialogRef.close();
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
