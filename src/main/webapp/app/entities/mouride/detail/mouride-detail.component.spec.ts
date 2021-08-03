import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MourideDetailComponent } from './mouride-detail.component';

describe('Component Tests', () => {
  describe('Mouride Management Detail Component', () => {
    let comp: MourideDetailComponent;
    let fixture: ComponentFixture<MourideDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MourideDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ mouride: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MourideDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MourideDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mouride on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mouride).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
