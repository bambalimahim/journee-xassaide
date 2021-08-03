import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CuisineDetailComponent } from './cuisine-detail.component';

describe('Component Tests', () => {
  describe('Cuisine Management Detail Component', () => {
    let comp: CuisineDetailComponent;
    let fixture: ComponentFixture<CuisineDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CuisineDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ cuisine: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CuisineDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CuisineDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cuisine on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cuisine).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
