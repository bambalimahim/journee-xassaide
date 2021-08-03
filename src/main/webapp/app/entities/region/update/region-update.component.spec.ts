jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RegionService } from '../service/region.service';
import { IRegion, Region } from '../region.model';
import { ILieu } from 'app/entities/lieu/lieu.model';
import { LieuService } from 'app/entities/lieu/service/lieu.service';

import { RegionUpdateComponent } from './region-update.component';

describe('Component Tests', () => {
  describe('Region Management Update Component', () => {
    let comp: RegionUpdateComponent;
    let fixture: ComponentFixture<RegionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let regionService: RegionService;
    let lieuService: LieuService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RegionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RegionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RegionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      regionService = TestBed.inject(RegionService);
      lieuService = TestBed.inject(LieuService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call lieu query and add missing value', () => {
        const region: IRegion = { id: 456 };
        const lieu: ILieu = { id: 81138 };
        region.lieu = lieu;

        const lieuCollection: ILieu[] = [{ id: 92075 }];
        jest.spyOn(lieuService, 'query').mockReturnValue(of(new HttpResponse({ body: lieuCollection })));
        const expectedCollection: ILieu[] = [lieu, ...lieuCollection];
        jest.spyOn(lieuService, 'addLieuToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ region });
        comp.ngOnInit();

        expect(lieuService.query).toHaveBeenCalled();
        expect(lieuService.addLieuToCollectionIfMissing).toHaveBeenCalledWith(lieuCollection, lieu);
        expect(comp.lieusCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const region: IRegion = { id: 456 };
        const lieu: ILieu = { id: 15722 };
        region.lieu = lieu;

        activatedRoute.data = of({ region });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(region));
        expect(comp.lieusCollection).toContain(lieu);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Region>>();
        const region = { id: 123 };
        jest.spyOn(regionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ region });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: region }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(regionService.update).toHaveBeenCalledWith(region);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Region>>();
        const region = new Region();
        jest.spyOn(regionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ region });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: region }));
        saveSubject.complete();

        // THEN
        expect(regionService.create).toHaveBeenCalledWith(region);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Region>>();
        const region = { id: 123 };
        jest.spyOn(regionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ region });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(regionService.update).toHaveBeenCalledWith(region);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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
