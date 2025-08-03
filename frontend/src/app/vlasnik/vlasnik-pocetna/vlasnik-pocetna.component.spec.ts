import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VlasnikPocetnaComponent } from './vlasnik-pocetna.component';

describe('VlasnikPocetnaComponent', () => {
  let component: VlasnikPocetnaComponent;
  let fixture: ComponentFixture<VlasnikPocetnaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VlasnikPocetnaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VlasnikPocetnaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
