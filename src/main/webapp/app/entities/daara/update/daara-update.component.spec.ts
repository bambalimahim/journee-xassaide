jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DaaraService } from '../service/daara.service';
import { IDaara, Daara } from '../daara.model';
import { IRegion } from 'app/entities/region/region.model';
import { RegionService } from 'app/entities/region/service/region.service';

import { DaaraUpdateComponent } from './daara-update.component';

describe('Component Tests', () => {
  describe('Daara Management Update Component', () => {
    let comp: DaaraUpdateComponent;
    let fixture: ComponentFixture<DaaraUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let daaraService: DaaraService;
    let regionService: RegionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DaaraUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DaaraUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DaaraUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      daaraService = TestBed.inject(DaaraService);
      regionService = TestBed.inject(RegionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Region query and add missing value', () => {
        const daara: IDaara = { id: 456 };
        const region: IRegion = { id: 50798 };
        daara.region = region;

        const regionCollection: IRegion[] = [{ id: 10168 }];
        jest.spyOn(regionService, 'query').mockReturnValue(of(new HttpResponse({ body: regionCollection })));
        const additionalRegions = [region];
        const expectedCollection: IRegion[] = [...additionalRegions, ...regionCollection];
        jest.spyOn(regionService, 'addRegionToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ daara });
        comp.ngOnInit();

        expect(regionService.query).toHaveBeenCalled();
        expect(regionService.addRegionToCollectionIfMissing).toHaveBeenCalledWith(regionCollection, ...additionalRegions);
        expect(comp.regionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const daara: IDaara = { id: 456 };
        const region: IRegion = { id: 18242 };
        daara.region = region;

        activatedRoute.data = of({ daara });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(daara));
        expect(comp.regionsSharedCollection).toContain(region);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Daara>>();
        const daara = { id: 123 };
        jest.spyOn(daaraService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ daara });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: daara }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(daaraService.update).toHaveBeenCalledWith(daara);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Daara>>();
        const daara = new Daara();
        jest.spyOn(daaraService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ daara });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: daara }));
        saveSubject.complete();

        // THEN
        expect(daaraService.create).toHaveBeenCalledWith(daara);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Daara>>();
        const daara = { id: 123 };
        jest.spyOn(daaraService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ daara });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(daaraService.update).toHaveBeenCalledWith(daara);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackRegionById', () => {
        it('Should return tracked Region primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackRegionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
