import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImageSequenceGameComponent } from './image-sequence-game.component';

describe('ImageSequenceGameComponent', () => {
  let component: ImageSequenceGameComponent;
  let fixture: ComponentFixture<ImageSequenceGameComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ImageSequenceGameComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ImageSequenceGameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
