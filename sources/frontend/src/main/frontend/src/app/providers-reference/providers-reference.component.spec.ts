import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProvidersReferenceComponent } from './providers-reference.component';

describe('SubscribeComponent', () => {
  let component: ProvidersReferenceComponent;
  let fixture: ComponentFixture<ProvidersReferenceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProvidersReferenceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProvidersReferenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
