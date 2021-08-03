jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MourideService } from '../service/mouride.service';
import { IMouride, Mouride } from '../mouride.model';
import { ICuisine } from 'app/entities/cuisine/cuisine.model';
import { CuisineService } from 'app/entities/cuisine/service/cuisine.service';

import { MourideUpdateComponent } from './mouride-update.component';

describe('Component Tests', () => {
  describe('Mouride Management Update Component', () => {
    let comp: MourideUpdateComponent;
    let fixture: ComponentFixture<MourideUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let mourideService: MourideService;
    let cuisineService: CuisineService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MourideUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MourideUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MourideUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      mourideService = TestBed.inject(MourideService);
      cuisineService = TestBed.inject(CuisineService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Cuisine query and add missing value', () => {
        const mouride: IMouride = { id: 456 };
        const cuisine: ICuisine = { id: 89832 };
        mouride.cuisine = cuisine;

        const cuisineCollection: ICuisine[] = [{ id: 27891 }];
        jest.spyOn(cuisineService, 'query').mockReturnValue(of(new HttpResponse({ body: cuisineCollection })));
        const additionalCuisines = [cuisine];
        const expectedCollection: ICuisine[] = [...additionalCuisines, ...cuisineCollection];
        jest.spyOn(cuisineService, 'addCuisineToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ mouride });
        comp.ngOnInit();

        expect(cuisineService.query).toHaveBeenCalled();
        expect(cuisineService.addCuisineToCollectionIfMissing).toHaveBeenCalledWith(cuisineCollection, ...additionalCuisines);
        expect(comp.cuisinesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const mouride: IMouride = { id: 456 };
        const cuisine: ICuisine = { id: 27596 };
        mouride.cuisine = cuisine;

        activatedRoute.data = of({ mouride });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(mouride));
        expect(comp.cuisinesSharedCollection).toContain(cuisine);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Mouride>>();
        const mouride = { id: 123 };
        jest.spyOn(mourideService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mouride });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mouride }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(mourideService.update).toHaveBeenCalledWith(mouride);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Mouride>>();
        const mouride = new Mouride();
        jest.spyOn(mourideService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mouride });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mouride }));
        saveSubject.complete();

        // THEN
        expect(mourideService.create).toHaveBeenCalledWith(mouride);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Mouride>>();
        const mouride = { id: 123 };
        jest.spyOn(mourideService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mouride });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(mourideService.update).toHaveBeenCalledWith(mouride);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCuisineById', () => {
        it('Should return tracked Cuisine primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCuisineById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
