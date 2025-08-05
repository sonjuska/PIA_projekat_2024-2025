import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UrediMojuVikendicuComponent } from './uredi-moju-vikendicu.component';

describe('UrediMojuVikendicuComponent', () => {
  let component: UrediMojuVikendicuComponent;
  let fixture: ComponentFixture<UrediMojuVikendicuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UrediMojuVikendicuComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UrediMojuVikendicuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
