import {Component, OnInit} from "@angular/core";

enum ProfilePage {
  Documents,
  Informations,
  Password,
  Contact
}
class Page{
  constructor(public type: ProfilePage){
  }
}

@Component({
  selector: 'app-profile-provider',
  templateUrl: './profile-provider.component.html',
  styleUrls: ['./profile-provider.component.css']
})
export class ProfileProviderComponent implements OnInit{

  pageType = ProfilePage;
  public pageActivated : Page;

  constructor(){
    this.pageActivated = new Page(ProfilePage.Informations);
  }

  ngOnInit() {

  }

  changePage(newPage : ProfilePage){
    // console.log("Old type : " + this.pageActivated.type);
    this.pageActivated.type = newPage;
    // console.log("New type : " + this.pageActivated.type);
  }
}
