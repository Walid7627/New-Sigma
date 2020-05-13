import { Component, OnInit } from "@angular/core";
import { AuthService } from "../../core/auth/auth.service";
import { ProfileProviderService } from '../profile-provider.service';
import { HttpResponse, HttpEventType } from '@angular/common/http';
import { DomSanitizer } from '@angular/platform-browser';


@Component({
  selector: 'app-provider-main',
  templateUrl: './profile-main.component.html',
  styleUrls: ['./profile-main.component.css']
})

export class ProfileMainComponent implements OnInit{

  provider;
  logo;

  constructor(private authService: AuthService, private profileProviderService: ProfileProviderService,
              private domSanitizer: DomSanitizer){
  }

  ngOnInit() {
    this.profileProviderService.get().subscribe(res => {

      if (res.type === HttpEventType.Response) {
        let data:any = res.body;

        if (data.status === "OK") {
          this.provider = JSON.parse(data.message);

          this.profileProviderService.getDocument(this.provider.logo).subscribe(res => {

            if(res.type === HttpEventType.Response) {
              let documentData: any = res;
              this.logo = URL.createObjectURL(documentData.body);
            }
          });
          console.log("provider value:");
          console.log(this.provider);
        } else {
          console.log("Error while loading profile");
          console.log(data);
        }
      }

    },

    err => {
      console.log("Error while loading profile");
      console.log(err);
    });
  }

}
