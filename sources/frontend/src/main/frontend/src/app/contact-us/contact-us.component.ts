import { Component, OnInit, ViewChild } from '@angular/core';

import { ContactusService } from '../service/contactus.service';
import { FormGroup, FormControl, FormBuilder, Validators, PatternValidator, ValidatorFn } from '@angular/forms';
import {MatDialogRef} from '@angular/material/dialog';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { UserService } from '../service/user.service';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';

@Component({
  selector: 'app-contact-us',
  templateUrl: './contact-us.component.html',
  styleUrls: ['./contact-us.component.css']
})
export class ContactUsComponent implements OnInit {
    contact = {
      nom:"",
      telephone:"",
      mail:"",
      message:"",
      numSiret:"",
      nomSociete:""
    }
    form: FormGroup;
    loading: boolean;
    config : PerfectScrollbarConfigInterface = {};
    @ViewChild(ToastContainerDirective, { static: true }) toastContainer: ToastContainerDirective;
    constructor(private formBuilder: FormBuilder, public contactus:ContactusService, public dialogRef: MatDialogRef<ContactUsComponent>, private userSercice : UserService, private toastrService: ToastrService) { }


  ngOnInit() {
    this.loading = false;
    this.toastrService.overlayContainer = this.toastContainer;
    this.form = this.formBuilder.group({
      nom : ['', Validators.required],
      numSiret: ['', [Validators.required, Validators.minLength(14), Validators.maxLength(14)]],
      nomSociete : ['', Validators.required],
      message : ['', [Validators.required, Validators.maxLength(2000)]],
      telephone:   ['', [Validators.required, Validators.pattern("([0-9]{2}\\s){4}[0-9]{2}"), Validators.minLength(14), Validators.maxLength(14)]],
      email: ['', [Validators.required, Validators.pattern("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")]],

    });
  }

  onSubmit() {
    this.loading = true;
    console.log(this.loading);
    this.contactus.save(this.contact)
    .subscribe(data => {
      this.showToastSuccessMessage("Votre demande de contact a été bien enregistrée", "Demande de contact");
      this.form.reset();
      this.loading = false;
    }, error => {
      this.showToastErrorMessage("Erreur !", "Demande de contact");

      console.log('Error: ');
      console.log(error);
      this.loading = false;
    });
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
