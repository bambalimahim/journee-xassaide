jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DroussService } from '../service/drouss.service';
import { IDrouss, Drouss } from '../drouss.model';
import { IXassaide } from 'app/entities/xassaide/xassaide.model';
import { XassaideService } from 'app/entities/xassaide/service/xassaide.service';
import { ILieu } from 'app/entities/lieu/lieu.model';
import { LieuService } from 'app/entities/lieu/service/lieu.service';

import { DroussUpdateComponent } from './drouss-update.component';

describe('Component Tests', () => {
  describe('Drouss Management Update Component', () => {
    let comp: DroussUpdateComponent;
    let fixture: ComponentFixture<DroussUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let droussService: DroussService;
    let xassaideService: XassaideService;
    let lieuService: LieuService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DroussUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DroussUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DroussUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      droussService = TestBed.inject(DroussService);
      xassaideService = TestBed.inject(XassaideService);
      lieuService = TestBed.inject(LieuService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Xassaide query and add missing value', () => {
        const drouss: IDrouss = { id: 456 };
        const xassaide: IXassaide = { id: 16453 };
        drouss.xassaide = xassaide;

        const xassaideCollection: IXassaide[] = [{ id: 115 }];
        jest.spyOn(xassaideService, 'query').mockReturnValue(of(new HttpResponse({ body: xassaideCollection })));
        const additionalXassaides = [xassaide];
        const expectedCollection: IXassaide[] = [...additionalXassaides, ...xassaideCollection];
        jest.spyOn(xassaideService, 'addXassaideToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ drouss });
        comp.ngOnInit();

        expect(xassaideService.query).toHaveBeenCalled();
        expect(xassaideService.addXassaideToCollectionIfMissing).toHaveBeenCalledWith(xassaideCollection, ...additionalXassaides);
        expect(comp.xassaidesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Lieu query and add missing value', () => {
        const drouss: IDrouss = { id: 456 };
        const lieu: ILieu = { id: 47851 };
        drouss.lieu = lieu;

        const lieuCollection: ILieu[] = [{ id: 54466 }];
        jest.spyOn(lieuService, 'query').mockReturnValue(of(new HttpResponse({ body: lieuCollection })));
        const additionalLieus = [lieu];
        const expectedCollection: ILieu[] = [...additionalLieus, ...lieuCollection];
        jest.spyOn(lieuService, 'addLieuToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ drouss });
        comp.ngOnInit();

        expect(lieuService.query).toHaveBeenCalled();
        expect(lieuService.addLieuToCollectionIfMissing).toHaveBeenCalledWith(lieuCollection, ...additionalLieus);
        expect(comp.lieusSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const drouss: IDrouss = { id: 456 };
        const xassaide: IXassaide = { id: 33596 };
        drouss.xassaide = xassaide;
        const lieu: ILieu = { id: 50069 };
        drouss.lieu = lieu;

        activatedRoute.data = of({ drouss });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(drouss));
        expect(comp.xassaidesSharedCollection).toContain(xassaide);
        expect(comp.lieusSharedCollection).toContain(lieu);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Drouss>>();
        const drouss = { id: 123 };
        jest.spyOn(droussService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ drouss });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: drouss }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(droussService.update).toHaveBeenCalledWith(drouss);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Drouss>>();
        const drouss = new Drouss();
        jest.spyOn(droussService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ drouss });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: drouss }));
        saveSubject.complete();

        // THEN
        expect(droussService.create).toHaveBeenCalledWith(drouss);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Drouss>>();
        const drouss = { id: 123 };
        jest.spyOn(droussService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ drouss });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(droussService.update).toHaveBeenCalledWith(drouss);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackXassaideById', () => {
        it('Should return tracked Xassaide primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackXassaideById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackLieuById', () => {
        it('Should return tracked Lieu primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLieuById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
