import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminsEntityListComponent } from './admins-entity-list.component';

describe('AdminsEntityListComponent', () => {
  let component: AdminsEntityListComponent;
  let fixture: ComponentFixture<AdminsEntityListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminsEntityListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminsEntityListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
