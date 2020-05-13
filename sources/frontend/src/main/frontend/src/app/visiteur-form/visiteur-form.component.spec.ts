import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { VisiteursFormComponent } from './visiteur-form.component';

describe('VisiteursFormComponent', () => {
  let component: VisiteursFormComponent;
  let fixture: ComponentFixture<VisiteursFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VisiteursFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VisiteursFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
