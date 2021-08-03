import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KourelDetailComponent } from './kourel-detail.component';

describe('Component Tests', () => {
  describe('Kourel Management Detail Component', () => {
    let comp: KourelDetailComponent;
    let fixture: ComponentFixture<KourelDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [KourelDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ kourel: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(KourelDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(KourelDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load kourel on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.kourel).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
