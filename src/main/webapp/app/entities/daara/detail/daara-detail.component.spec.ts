import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DaaraDetailComponent } from './daara-detail.component';

describe('Component Tests', () => {
  describe('Daara Management Detail Component', () => {
    let comp: DaaraDetailComponent;
    let fixture: ComponentFixture<DaaraDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DaaraDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ daara: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DaaraDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DaaraDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load daara on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.daara).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
