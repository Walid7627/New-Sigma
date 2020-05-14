import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PurchaserFormComponent } from './provider-show-qualification.component';

describe('PurchaserFormComponent', () => {
  let component: PurchaserFormComponent;
  let fixture: ComponentFixture<PurchaserFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PurchaserFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PurchaserFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
