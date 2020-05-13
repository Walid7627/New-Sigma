import { BrowserModule } from '@angular/platform-browser';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule} from '@angular/material/button';
import { MatSortModule} from '@angular/material/sort';
import { MatIconModule} from '@angular/material/icon';
import { MatCheckboxModule} from '@angular/material/checkbox';
import { MatPaginatorModule} from '@angular/material/paginator';
import { MatToolbarModule} from '@angular/material/toolbar';
import { MatInputModule} from '@angular/material/input';
import { MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { MatCardModule} from '@angular/material/card';
import { MatMenuModule} from '@angular/material/menu';
import { MatDialogModule} from '@angular/material/dialog';
import { MatSnackBarModule} from '@angular/material/snack-bar';
import { MatTableModule} from '@angular/material/table';

import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
//import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { SubscribeComponent } from './subscribe/subscribe.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { ContactUsComponent } from './contact-us/contact-us.component';
import {  NavbarComponent } from './navbar/navbar.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
// import { LoginService } from './service/login.service';

import { AuthService } from './core/auth/auth.service';
import { RoleService } from './core/role/role.service';
import { Interceptor } from './core/interceptor/interceptor';
import { TokenStorage } from './core/token/token.storage';
import { UserStorage } from './core/userstorage/user.storage';

import { ProviderService } from './service/provider.service';
import { UserService } from './service/user.service';
import { ContactusService } from './service/contactus.service';
import { CodeAPEService } from './service/code-ape.service';
import { CodeCPVService } from './service/code-cpv.service';

//import { RoleUserService } from './service/role-user.service';



//import { RoleComponent } from './role/role.component';
import { FileUploadService } from './service/file-upload.service';
//import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AuthGuard } from './core/auth/auth.guard';
import { RoleGuard } from './core/role/role.guard';
import { ReservedTestComponent } from './reserved/reserved.component';
import {ProfileProviderComponent} from './profile-provider/profile-provider.component';
// import {ListProvidersModule} from "./list-providers/list-providers.module";
import {ProvidersListComponent} from "./providers-list/providers-list.component";
import { EntityFormComponent } from './entity-form/entity-form.component';
import { EntitiesListComponent } from './entities-list/entities-list.component';
import { ProfileProviderService } from './profile-provider/profile-provider.service';
import { ProfileDocumentsComponent } from './profile-provider/provider-documents/profile-documents.component';
import { ProviderDocumentsComponent } from './provider-documents/profile-documents.component';
import { ProfileInformationsComponent } from './profile-provider/provider-informations/profile-informations.component';
import { ProfileMainComponent } from './profile-provider/provider-main/profile-main.component';
import { ProfilePasswordComponent } from './profile-provider/provider-password/profile-password.component';
import { FooterComponent } from './footer/footer.component';
import { AboutComponent } from './about/about.component';
import { ProvidersReferenceComponent } from './providers-reference/providers-reference.component';
import { ProviderContactComponent } from './provider-contact/provider-contact.component';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatListModule} from '@angular/material/list';
import { NgSelectModule } from '@ng-select/ng-select';
import { AdminsEntityListComponent } from './admins-entity-list/admins-entity-list.component';
import { AdminsEntityFormComponent } from './admin-entity-form/admin-entity-form.component';
import { AdminEntityService } from './service/admin-entity.service';
import { MatConfirmDialogComponent } from './mat-confirm-dialog/mat-confirm-dialog.component';
import { DialogService } from './service/dialog-service';
import { EntityAffectComponent } from './entity-affect/entity-affect.component';
import { ToastrModule, ToastContainerModule } from 'ngx-toastr';

import { PERFECT_SCROLLBAR_CONFIG, PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { ProfileProviderContactComponent } from './profile-provider/provider-contact/provider-contact.component';
import { NewPasswordComponent } from './new-password/new-password.component';

import { CarouselModule, WavesModule, MDBBootstrapModule } from 'angular-bootstrap-md'
import { PurchaserListComponent } from './purchaser-list/purchaser-list.component';
import { PurchaserService } from './service/purchaser.service';
import { TeamListComponent } from './teams-list/teams-list.component';
import { TeamFormComponent } from './teams-form/team-form.component';
import {TeamPurchaserComponent} from "./teams-purchaser/team-purchaser.component";
import { SegmentsListComponent } from './segments-list/segments-list.component';
import { TeamService } from './service/team.service';
import { SegmentService } from './service/segment.service';
import { PurchaserFormComponent } from './purchaser-form/purchaser-form.component';
import { VisiteurService } from './service/visiteur.service';
import { VisiteursListComponent } from './visiteurs-list/visiteurs-list.component';
import { VisiteursFormComponent } from './visiteur-form/visiteur-form.component';
import { SegmentComponent } from './segment/segment.component';
import { ProvidersSegmentComponent } from './providers-segment/providers-segment.component';

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true
};

const appRoutes: Routes = [
  { path: 'home/:token', component:  HomeComponent},
  { path: 'inscription', component:  SubscribeComponent},
  { path: 'connexion', component:  LoginComponent},

  //{ path: 'role', component: RoleComponent},
  { path: 'providers', component: ProvidersListComponent},
  { path: 'entities', component: EntitiesListComponent},
  { path: 'admins-entity', component: AdminsEntityListComponent},
  { path: 'profil', component: ProfileProviderComponent},
  { path: 'new_password/:mail', component: NewPasswordComponent},
  { path: 'purchasers', component: PurchaserListComponent},
  { path: 'teams', component: TeamListComponent},
  { path: 'segments', component: SegmentsListComponent},
  { path: 'visiteurs', component: VisiteursListComponent},

  {
    path: 'reserved',
    component: ReservedTestComponent,
    canActivate: [AuthGuard]
  },
  { path: '', redirectTo: '/accueil', pathMatch: 'full'},
  { path: '**', component: HomeComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    SubscribeComponent,
    LoginComponent,
    HomeComponent,
    ReservedTestComponent,
    NavbarComponent,
    ContactUsComponent,
    ProfileProviderComponent,
    ProfileDocumentsComponent,
    ProfileInformationsComponent,
    ProviderDocumentsComponent,
    ProfileMainComponent,
    ProfilePasswordComponent,
    EntityFormComponent,
    EntitiesListComponent,
    AdminsEntityListComponent,
    AdminsEntityFormComponent,
    ProviderContactComponent,
    PurchaserListComponent,
    TeamListComponent,
    TeamFormComponent,
    TeamPurchaserComponent,
    SegmentsListComponent,
    SegmentComponent,
    PurchaserFormComponent,
    VisiteursListComponent,
    VisiteursFormComponent,
    //RoleComponent,
    ProvidersListComponent,
    ProvidersReferenceComponent,
    FooterComponent,
    AboutComponent,
    MatConfirmDialogComponent,
    EntityAffectComponent,
    ProfileProviderContactComponent,
    NewPasswordComponent,
    SegmentComponent,
    ProvidersSegmentComponent,
  ],
  imports: [
    FormsModule,
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatButtonModule,
    CommonModule,
    MatIconModule,
    MatCheckboxModule,
    RouterModule.forRoot(appRoutes),
    MatToolbarModule,
    MatInputModule,
    MatTableModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatCardModule,
    MatMenuModule,
    MatSortModule,
    MatDialogModule,
    MatSnackBarModule,
    HttpClientModule,
    BrowserAnimationsModule,
    CommonModule,
    NgSelectModule,
    MatExpansionModule,
    MatListModule,
    PerfectScrollbarModule,
    ToastrModule.forRoot({ positionClass: 'inline' }),
    ToastContainerModule,
    MDBBootstrapModule.forRoot(),
    CarouselModule.forRoot(),
    WavesModule.forRoot()
  ],
  providers: [
    CodeCPVService,
    CodeAPEService,
    ProfileProviderService,
    ProviderService,
    UserService,
    AuthService,
    RoleService,
    RoleGuard,
    TokenStorage,
    UserStorage,
    AdminEntityService,
    VisiteurService,
    PurchaserService,
    TeamService,
    SegmentService,
    AuthGuard,
    DialogService,
    ContactusService,
    FileUploadService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: Interceptor,
      multi : true
    },
    {
      provide: PERFECT_SCROLLBAR_CONFIG,
      useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG
    }
  ],
  entryComponents : [PurchaserFormComponent, TeamFormComponent, TeamPurchaserComponent, EntityFormComponent, ContactUsComponent, AboutComponent, ProvidersReferenceComponent, AdminsEntityFormComponent, MatConfirmDialogComponent, EntityAffectComponent, ProviderContactComponent,ProviderDocumentsComponent,VisiteursFormComponent],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA]
})
export class AppModule { }
