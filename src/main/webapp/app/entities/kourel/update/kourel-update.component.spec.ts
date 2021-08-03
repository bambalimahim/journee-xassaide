jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { KourelService } from '../service/kourel.service';
import { IKourel, Kourel } from '../kourel.model';
import { IDaara } from 'app/entities/daara/daara.model';
import { DaaraService } from 'app/entities/daara/service/daara.service';

import { KourelUpdateComponent } from './kourel-update.component';

describe('Component Tests', () => {
  describe('Kourel Management Update Component', () => {
    let comp: KourelUpdateComponent;
    let fixture: ComponentFixture<KourelUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let kourelService: KourelService;
    let daaraService: DaaraService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [KourelUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(KourelUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(KourelUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      kourelService = TestBed.inject(KourelService);
      daaraService = TestBed.inject(DaaraService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Daara query and add missing value', () => {
        const kourel: IKourel = { id: 456 };
        const daara: IDaara = { id: 92383 };
        kourel.daara = daara;

        const daaraCollection: IDaara[] = [{ id: 58959 }];
        jest.spyOn(daaraService, 'query').mockReturnValue(of(new HttpResponse({ body: daaraCollection })));
        const additionalDaaras = [daara];
        const expectedCollection: IDaara[] = [...additionalDaaras, ...daaraCollection];
        jest.spyOn(daaraService, 'addDaaraToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ kourel });
        comp.ngOnInit();

        expect(daaraService.query).toHaveBeenCalled();
        expect(daaraService.addDaaraToCollectionIfMissing).toHaveBeenCalledWith(daaraCollection, ...additionalDaaras);
        expect(comp.daarasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const kourel: IKourel = { id: 456 };
        const daara: IDaara = { id: 57855 };
        kourel.daara = daara;

        activatedRoute.data = of({ kourel });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(kourel));
        expect(comp.daarasSharedCollection).toContain(daara);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Kourel>>();
        const kourel = { id: 123 };
        jest.spyOn(kourelService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ kourel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: kourel }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(kourelService.update).toHaveBeenCalledWith(kourel);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Kourel>>();
        const kourel = new Kourel();
        jest.spyOn(kourelService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ kourel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: kourel }));
        saveSubject.complete();

        // THEN
        expect(kourelService.create).toHaveBeenCalledWith(kourel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Kourel>>();
        const kourel = { id: 123 };
        jest.spyOn(kourelService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ kourel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(kourelService.update).toHaveBeenCalledWith(kourel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackDaaraById', () => {
        it('Should return tracked Daara primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDaaraById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
