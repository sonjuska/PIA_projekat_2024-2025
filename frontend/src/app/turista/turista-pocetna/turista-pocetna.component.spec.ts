import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TuristaPocetnaComponent } from './turista-pocetna.component';

describe('TuristaPocetnaComponent', () => {
  let component: TuristaPocetnaComponent;
  let fixture: ComponentFixture<TuristaPocetnaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TuristaPocetnaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TuristaPocetnaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
