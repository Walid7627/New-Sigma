import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EntityAffectComponent } from './entity-affect.component';

describe('EntityAffectComponent', () => {
  let component: EntityAffectComponent;
  let fixture: ComponentFixture<EntityAffectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EntityAffectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EntityAffectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
