jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { XassaideService } from '../service/xassaide.service';
import { IXassaide, Xassaide } from '../xassaide.model';

import { XassaideUpdateComponent } from './xassaide-update.component';

describe('Component Tests', () => {
  describe('Xassaide Management Update Component', () => {
    let comp: XassaideUpdateComponent;
    let fixture: ComponentFixture<XassaideUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let xassaideService: XassaideService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [XassaideUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(XassaideUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(XassaideUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      xassaideService = TestBed.inject(XassaideService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const xassaide: IXassaide = { id: 456 };

        activatedRoute.data = of({ xassaide });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(xassaide));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Xassaide>>();
        const xassaide = { id: 123 };
        jest.spyOn(xassaideService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ xassaide });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: xassaide }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(xassaideService.update).toHaveBeenCalledWith(xassaide);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Xassaide>>();
        const xassaide = new Xassaide();
        jest.spyOn(xassaideService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ xassaide });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: xassaide }));
        saveSubject.complete();

        // THEN
        expect(xassaideService.create).toHaveBeenCalledWith(xassaide);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Xassaide>>();
        const xassaide = { id: 123 };
        jest.spyOn(xassaideService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ xassaide });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(xassaideService.update).toHaveBeenCalledWith(xassaide);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
