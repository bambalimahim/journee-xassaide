import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { XassaideDetailComponent } from './xassaide-detail.component';

describe('Component Tests', () => {
  describe('Xassaide Management Detail Component', () => {
    let comp: XassaideDetailComponent;
    let fixture: ComponentFixture<XassaideDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [XassaideDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ xassaide: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(XassaideDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(XassaideDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load xassaide on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.xassaide).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
