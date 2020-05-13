import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reserved-component',
  templateUrl: './reserved.component.html',
  styleUrls: ['./reserved.component.css']
})
export class ReservedTestComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {
  }


}
