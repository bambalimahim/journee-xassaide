import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDaara, Daara } from '../daara.model';

import { DaaraService } from './daara.service';

describe('Service Tests', () => {
  describe('Daara Service', () => {
    let service: DaaraService;
    let httpMock: HttpTestingController;
    let elemDefault: IDaara;
    let expectedResult: IDaara | IDaara[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DaaraService);
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

      it('should create a Daara', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Daara()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Daara', () => {
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

      it('should partial update a Daara', () => {
        const patchObject = Object.assign({}, new Daara());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Daara', () => {
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

      it('should delete a Daara', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDaaraToCollectionIfMissing', () => {
        it('should add a Daara to an empty array', () => {
          const daara: IDaara = { id: 123 };
          expectedResult = service.addDaaraToCollectionIfMissing([], daara);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(daara);
        });

        it('should not add a Daara to an array that contains it', () => {
          const daara: IDaara = { id: 123 };
          const daaraCollection: IDaara[] = [
            {
              ...daara,
            },
            { id: 456 },
          ];
          expectedResult = service.addDaaraToCollectionIfMissing(daaraCollection, daara);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Daara to an array that doesn't contain it", () => {
          const daara: IDaara = { id: 123 };
          const daaraCollection: IDaara[] = [{ id: 456 }];
          expectedResult = service.addDaaraToCollectionIfMissing(daaraCollection, daara);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(daara);
        });

        it('should add only unique Daara to an array', () => {
          const daaraArray: IDaara[] = [{ id: 123 }, { id: 456 }, { id: 51427 }];
          const daaraCollection: IDaara[] = [{ id: 123 }];
          expectedResult = service.addDaaraToCollectionIfMissing(daaraCollection, ...daaraArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const daara: IDaara = { id: 123 };
          const daara2: IDaara = { id: 456 };
          expectedResult = service.addDaaraToCollectionIfMissing([], daara, daara2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(daara);
          expect(expectedResult).toContain(daara2);
        });

        it('should accept null and undefined values', () => {
          const daara: IDaara = { id: 123 };
          expectedResult = service.addDaaraToCollectionIfMissing([], null, daara, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(daara);
        });

        it('should return initial array if no Daara is added', () => {
          const daaraCollection: IDaara[] = [{ id: 123 }];
          expectedResult = service.addDaaraToCollectionIfMissing(daaraCollection, undefined, null);
          expect(expectedResult).toEqual(daaraCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
