import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PacientesCardComponent } from './pacientes-card.component';

describe('PacientesCardComponent', () => {
  let component: PacientesCardComponent;
  let fixture: ComponentFixture<PacientesCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PacientesCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PacientesCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
