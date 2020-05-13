import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SegmentsListComponent } from './segments-list.component';

describe('SegmentListComponent', () => {
  let component: SegmentsListComponent;
  let fixture: ComponentFixture<SegmentsListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SegmentsListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SegmentsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
