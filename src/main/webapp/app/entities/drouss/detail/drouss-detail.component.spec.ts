import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DroussDetailComponent } from './drouss-detail.component';

describe('Component Tests', () => {
  describe('Drouss Management Detail Component', () => {
    let comp: DroussDetailComponent;
    let fixture: ComponentFixture<DroussDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DroussDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ drouss: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DroussDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DroussDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load drouss on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.drouss).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
