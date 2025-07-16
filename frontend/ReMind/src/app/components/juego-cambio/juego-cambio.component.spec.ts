import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JuegoCambioComponent } from './juego-cambio.component';

describe('JuegoCambioComponent', () => {
  let component: JuegoCambioComponent;
  let fixture: ComponentFixture<JuegoCambioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JuegoCambioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JuegoCambioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
