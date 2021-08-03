import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IXassaide, Xassaide } from '../xassaide.model';

import { XassaideService } from './xassaide.service';

describe('Service Tests', () => {
  describe('Xassaide Service', () => {
    let service: XassaideService;
    let httpMock: HttpTestingController;
    let elemDefault: IXassaide;
    let expectedResult: IXassaide | IXassaide[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(XassaideService);
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

      it('should create a Xassaide', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Xassaide()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Xassaide', () => {
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

      it('should partial update a Xassaide', () => {
        const patchObject = Object.assign({}, new Xassaide());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Xassaide', () => {
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

      it('should delete a Xassaide', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addXassaideToCollectionIfMissing', () => {
        it('should add a Xassaide to an empty array', () => {
          const xassaide: IXassaide = { id: 123 };
          expectedResult = service.addXassaideToCollectionIfMissing([], xassaide);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(xassaide);
        });

        it('should not add a Xassaide to an array that contains it', () => {
          const xassaide: IXassaide = { id: 123 };
          const xassaideCollection: IXassaide[] = [
            {
              ...xassaide,
            },
            { id: 456 },
          ];
          expectedResult = service.addXassaideToCollectionIfMissing(xassaideCollection, xassaide);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Xassaide to an array that doesn't contain it", () => {
          const xassaide: IXassaide = { id: 123 };
          const xassaideCollection: IXassaide[] = [{ id: 456 }];
          expectedResult = service.addXassaideToCollectionIfMissing(xassaideCollection, xassaide);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(xassaide);
        });

        it('should add only unique Xassaide to an array', () => {
          const xassaideArray: IXassaide[] = [{ id: 123 }, { id: 456 }, { id: 38175 }];
          const xassaideCollection: IXassaide[] = [{ id: 123 }];
          expectedResult = service.addXassaideToCollectionIfMissing(xassaideCollection, ...xassaideArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const xassaide: IXassaide = { id: 123 };
          const xassaide2: IXassaide = { id: 456 };
          expectedResult = service.addXassaideToCollectionIfMissing([], xassaide, xassaide2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(xassaide);
          expect(expectedResult).toContain(xassaide2);
        });

        it('should accept null and undefined values', () => {
          const xassaide: IXassaide = { id: 123 };
          expectedResult = service.addXassaideToCollectionIfMissing([], null, xassaide, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(xassaide);
        });

        it('should return initial array if no Xassaide is added', () => {
          const xassaideCollection: IXassaide[] = [{ id: 123 }];
          expectedResult = service.addXassaideToCollectionIfMissing(xassaideCollection, undefined, null);
          expect(expectedResult).toEqual(xassaideCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
