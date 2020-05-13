import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ProfileProviderContactComponent } from './provider-contact.component';



describe('ProfileProviderContactComponent', () => {
  let component: ProfileProviderContactComponent;
  let fixture: ComponentFixture<ProfileProviderContactComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProfileProviderContactComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileProviderContactComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
