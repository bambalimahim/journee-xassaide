jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CuisineService } from '../service/cuisine.service';
import { ICuisine, Cuisine } from '../cuisine.model';
import { ILieu } from 'app/entities/lieu/lieu.model';
import { LieuService } from 'app/entities/lieu/service/lieu.service';

import { CuisineUpdateComponent } from './cuisine-update.component';

describe('Component Tests', () => {
  describe('Cuisine Management Update Component', () => {
    let comp: CuisineUpdateComponent;
    let fixture: ComponentFixture<CuisineUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let cuisineService: CuisineService;
    let lieuService: LieuService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CuisineUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CuisineUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CuisineUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      cuisineService = TestBed.inject(CuisineService);
      lieuService = TestBed.inject(LieuService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call lieu query and add missing value', () => {
        const cuisine: ICuisine = { id: 456 };
        const lieu: ILieu = { id: 32838 };
        cuisine.lieu = lieu;

        const lieuCollection: ILieu[] = [{ id: 458 }];
        jest.spyOn(lieuService, 'query').mockReturnValue(of(new HttpResponse({ body: lieuCollection })));
        const expectedCollection: ILieu[] = [lieu, ...lieuCollection];
        jest.spyOn(lieuService, 'addLieuToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ cuisine });
        comp.ngOnInit();

        expect(lieuService.query).toHaveBeenCalled();
        expect(lieuService.addLieuToCollectionIfMissing).toHaveBeenCalledWith(lieuCollection, lieu);
        expect(comp.lieusCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const cuisine: ICuisine = { id: 456 };
        const lieu: ILieu = { id: 49402 };
        cuisine.lieu = lieu;

        activatedRoute.data = of({ cuisine });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(cuisine));
        expect(comp.lieusCollection).toContain(lieu);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Cuisine>>();
        const cuisine = { id: 123 };
        jest.spyOn(cuisineService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cuisine });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cuisine }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(cuisineService.update).toHaveBeenCalledWith(cuisine);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Cuisine>>();
        const cuisine = new Cuisine();
        jest.spyOn(cuisineService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cuisine });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cuisine }));
        saveSubject.complete();

        // THEN
        expect(cuisineService.create).toHaveBeenCalledWith(cuisine);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Cuisine>>();
        const cuisine = { id: 123 };
        jest.spyOn(cuisineService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cuisine });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(cuisineService.update).toHaveBeenCalledWith(cuisine);
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
