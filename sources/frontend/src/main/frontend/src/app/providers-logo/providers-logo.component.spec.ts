import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProvidersLogoComponent } from './providers-logo.component';

describe('ProvidersLogoComponent', () => {
  let component: ProvidersLogoComponent;
  let fixture: ComponentFixture<ProvidersLogoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProvidersLogoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProvidersLogoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
