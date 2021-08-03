import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICuisine, Cuisine } from '../cuisine.model';

import { CuisineService } from './cuisine.service';

describe('Service Tests', () => {
  describe('Cuisine Service', () => {
    let service: CuisineService;
    let httpMock: HttpTestingController;
    let elemDefault: ICuisine;
    let expectedResult: ICuisine | ICuisine[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CuisineService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nom: 'AAAAAAA',
        capacite: 0,
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

      it('should create a Cuisine', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Cuisine()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Cuisine', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            capacite: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Cuisine', () => {
        const patchObject = Object.assign({}, new Cuisine());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Cuisine', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            capacite: 1,
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

      it('should delete a Cuisine', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCuisineToCollectionIfMissing', () => {
        it('should add a Cuisine to an empty array', () => {
          const cuisine: ICuisine = { id: 123 };
          expectedResult = service.addCuisineToCollectionIfMissing([], cuisine);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cuisine);
        });

        it('should not add a Cuisine to an array that contains it', () => {
          const cuisine: ICuisine = { id: 123 };
          const cuisineCollection: ICuisine[] = [
            {
              ...cuisine,
            },
            { id: 456 },
          ];
          expectedResult = service.addCuisineToCollectionIfMissing(cuisineCollection, cuisine);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Cuisine to an array that doesn't contain it", () => {
          const cuisine: ICuisine = { id: 123 };
          const cuisineCollection: ICuisine[] = [{ id: 456 }];
          expectedResult = service.addCuisineToCollectionIfMissing(cuisineCollection, cuisine);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cuisine);
        });

        it('should add only unique Cuisine to an array', () => {
          const cuisineArray: ICuisine[] = [{ id: 123 }, { id: 456 }, { id: 66883 }];
          const cuisineCollection: ICuisine[] = [{ id: 123 }];
          expectedResult = service.addCuisineToCollectionIfMissing(cuisineCollection, ...cuisineArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const cuisine: ICuisine = { id: 123 };
          const cuisine2: ICuisine = { id: 456 };
          expectedResult = service.addCuisineToCollectionIfMissing([], cuisine, cuisine2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cuisine);
          expect(expectedResult).toContain(cuisine2);
        });

        it('should accept null and undefined values', () => {
          const cuisine: ICuisine = { id: 123 };
          expectedResult = service.addCuisineToCollectionIfMissing([], null, cuisine, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cuisine);
        });

        it('should return initial array if no Cuisine is added', () => {
          const cuisineCollection: ICuisine[] = [{ id: 123 }];
          expectedResult = service.addCuisineToCollectionIfMissing(cuisineCollection, undefined, null);
          expect(expectedResult).toEqual(cuisineCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
