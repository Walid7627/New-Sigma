import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PurchaserListComponent } from './purchaser-list.component';

describe('PurchaserListComponent', () => {
  let component: PurchaserListComponent;
  let fixture: ComponentFixture<PurchaserListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PurchaserListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PurchaserListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
