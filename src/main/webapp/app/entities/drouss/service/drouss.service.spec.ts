import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDrouss, Drouss } from '../drouss.model';

import { DroussService } from './drouss.service';

describe('Service Tests', () => {
  describe('Drouss Service', () => {
    let service: DroussService;
    let httpMock: HttpTestingController;
    let elemDefault: IDrouss;
    let expectedResult: IDrouss | IDrouss[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DroussService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nombre: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Drouss', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Drouss()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Drouss', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Drouss', () => {
        const patchObject = Object.assign(
          {
            nombre: 1,
          },
          new Drouss()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Drouss', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Drouss', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDroussToCollectionIfMissing', () => {
        it('should add a Drouss to an empty array', () => {
          const drouss: IDrouss = { id: 123 };
          expectedResult = service.addDroussToCollectionIfMissing([], drouss);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(drouss);
        });

        it('should not add a Drouss to an array that contains it', () => {
          const drouss: IDrouss = { id: 123 };
          const droussCollection: IDrouss[] = [
            {
              ...drouss,
            },
            { id: 456 },
          ];
          expectedResult = service.addDroussToCollectionIfMissing(droussCollection, drouss);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Drouss to an array that doesn't contain it", () => {
          const drouss: IDrouss = { id: 123 };
          const droussCollection: IDrouss[] = [{ id: 456 }];
          expectedResult = service.addDroussToCollectionIfMissing(droussCollection, drouss);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(drouss);
        });

        it('should add only unique Drouss to an array', () => {
          const droussArray: IDrouss[] = [{ id: 123 }, { id: 456 }, { id: 28967 }];
          const droussCollection: IDrouss[] = [{ id: 123 }];
          expectedResult = service.addDroussToCollectionIfMissing(droussCollection, ...droussArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const drouss: IDrouss = { id: 123 };
          const drouss2: IDrouss = { id: 456 };
          expectedResult = service.addDroussToCollectionIfMissing([], drouss, drouss2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(drouss);
          expect(expectedResult).toContain(drouss2);
        });

        it('should accept null and undefined values', () => {
          const drouss: IDrouss = { id: 123 };
          expectedResult = service.addDroussToCollectionIfMissing([], null, drouss, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(drouss);
        });

        it('should return initial array if no Drouss is added', () => {
          const droussCollection: IDrouss[] = [{ id: 123 }];
          expectedResult = service.addDroussToCollectionIfMissing(droussCollection, undefined, null);
          expect(expectedResult).toEqual(droussCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
