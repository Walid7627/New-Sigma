import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProvidersSegmentComponent } from './providers-segment.component';

describe('ProvidersSegmentComponent', () => {
  let component: ProvidersSegmentComponent;
  let fixture: ComponentFixture<ProvidersSegmentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProvidersSegmentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProvidersSegmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
