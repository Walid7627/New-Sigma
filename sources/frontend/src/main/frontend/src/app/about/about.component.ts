import { Component, OnInit } from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent implements OnInit {
  config : PerfectScrollbarConfigInterface = {};
  constructor(public dialogRef: MatDialogRef<AboutComponent>) { }

  ngOnInit() {
  }

  onClose() {
    this.dialogRef.close();
  }

}
