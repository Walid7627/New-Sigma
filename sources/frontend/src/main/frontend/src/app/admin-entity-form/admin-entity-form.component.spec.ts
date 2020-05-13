import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminsEntityFormComponent } from './admin-entity-form.component';

describe('AdminsEntityFormComponent', () => {
  let component: AdminsEntityFormComponent;
  let fixture: ComponentFixture<AdminsEntityFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminsEntityFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminsEntityFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
