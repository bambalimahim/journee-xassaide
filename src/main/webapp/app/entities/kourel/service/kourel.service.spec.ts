import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IKourel, Kourel } from '../kourel.model';

import { KourelService } from './kourel.service';

describe('Service Tests', () => {
  describe('Kourel Service', () => {
    let service: KourelService;
    let httpMock: HttpTestingController;
    let elemDefault: IKourel;
    let expectedResult: IKourel | IKourel[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(KourelService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nom: 'AAAAAAA',
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

      it('should create a Kourel', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Kourel()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Kourel', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Kourel', () => {
        const patchObject = Object.assign({}, new Kourel());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Kourel', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
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

      it('should delete a Kourel', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addKourelToCollectionIfMissing', () => {
        it('should add a Kourel to an empty array', () => {
          const kourel: IKourel = { id: 123 };
          expectedResult = service.addKourelToCollectionIfMissing([], kourel);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(kourel);
        });

        it('should not add a Kourel to an array that contains it', () => {
          const kourel: IKourel = { id: 123 };
          const kourelCollection: IKourel[] = [
            {
              ...kourel,
            },
            { id: 456 },
          ];
          expectedResult = service.addKourelToCollectionIfMissing(kourelCollection, kourel);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Kourel to an array that doesn't contain it", () => {
          const kourel: IKourel = { id: 123 };
          const kourelCollection: IKourel[] = [{ id: 456 }];
          expectedResult = service.addKourelToCollectionIfMissing(kourelCollection, kourel);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(kourel);
        });

        it('should add only unique Kourel to an array', () => {
          const kourelArray: IKourel[] = [{ id: 123 }, { id: 456 }, { id: 69416 }];
          const kourelCollection: IKourel[] = [{ id: 123 }];
          expectedResult = service.addKourelToCollectionIfMissing(kourelCollection, ...kourelArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const kourel: IKourel = { id: 123 };
          const kourel2: IKourel = { id: 456 };
          expectedResult = service.addKourelToCollectionIfMissing([], kourel, kourel2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(kourel);
          expect(expectedResult).toContain(kourel2);
        });

        it('should accept null and undefined values', () => {
          const kourel: IKourel = { id: 123 };
          expectedResult = service.addKourelToCollectionIfMissing([], null, kourel, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(kourel);
        });

        it('should return initial array if no Kourel is added', () => {
          const kourelCollection: IKourel[] = [{ id: 123 }];
          expectedResult = service.addKourelToCollectionIfMissing(kourelCollection, undefined, null);
          expect(expectedResult).toEqual(kourelCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
