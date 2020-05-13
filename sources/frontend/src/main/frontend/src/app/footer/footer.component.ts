import { Component, OnInit } from '@angular/core';
import { MatDialogConfig, MatDialog } from '@angular/material/dialog';
import { AboutComponent} from '../about/about.component';
import { ContactUsComponent} from '../contact-us/contact-us.component';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {

  constructor(private dialog: MatDialog) { }

  ngOnInit() {
  }

  displayAbout() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.width = "70%";
    this.dialog.open(AboutComponent,dialogConfig);
  }

  displayContact() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    
    dialogConfig.width = "70%";
    this.dialog.open(ContactUsComponent,dialogConfig);
  }

}
