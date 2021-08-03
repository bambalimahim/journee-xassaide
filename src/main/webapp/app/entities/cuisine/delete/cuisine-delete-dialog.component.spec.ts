jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CuisineService } from '../service/cuisine.service';

import { CuisineDeleteDialogComponent } from './cuisine-delete-dialog.component';

describe('Component Tests', () => {
  describe('Cuisine Management Delete Component', () => {
    let comp: CuisineDeleteDialogComponent;
    let fixture: ComponentFixture<CuisineDeleteDialogComponent>;
    let service: CuisineService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CuisineDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(CuisineDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CuisineDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CuisineService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
