import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMouride, Mouride } from '../mouride.model';

import { MourideService } from './mouride.service';

describe('Service Tests', () => {
  describe('Mouride Service', () => {
    let service: MourideService;
    let httpMock: HttpTestingController;
    let elemDefault: IMouride;
    let expectedResult: IMouride | IMouride[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MourideService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        prenom: 'AAAAAAA',
        nom: 'AAAAAAA',
        email: 'AAAAAAA',
        telephone: 'AAAAAAA',
        matricule: 'AAAAAAA',
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

      it('should create a Mouride', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Mouride()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Mouride', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            prenom: 'BBBBBB',
            nom: 'BBBBBB',
            email: 'BBBBBB',
            telephone: 'BBBBBB',
            matricule: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Mouride', () => {
        const patchObject = Object.assign(
          {
            prenom: 'BBBBBB',
            nom: 'BBBBBB',
            matricule: 'BBBBBB',
          },
          new Mouride()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Mouride', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            prenom: 'BBBBBB',
            nom: 'BBBBBB',
            email: 'BBBBBB',
            telephone: 'BBBBBB',
            matricule: 'BBBBBB',
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

      it('should delete a Mouride', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMourideToCollectionIfMissing', () => {
        it('should add a Mouride to an empty array', () => {
          const mouride: IMouride = { id: 123 };
          expectedResult = service.addMourideToCollectionIfMissing([], mouride);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mouride);
        });

        it('should not add a Mouride to an array that contains it', () => {
          const mouride: IMouride = { id: 123 };
          const mourideCollection: IMouride[] = [
            {
              ...mouride,
            },
            { id: 456 },
          ];
          expectedResult = service.addMourideToCollectionIfMissing(mourideCollection, mouride);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Mouride to an array that doesn't contain it", () => {
          const mouride: IMouride = { id: 123 };
          const mourideCollection: IMouride[] = [{ id: 456 }];
          expectedResult = service.addMourideToCollectionIfMissing(mourideCollection, mouride);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mouride);
        });

        it('should add only unique Mouride to an array', () => {
          const mourideArray: IMouride[] = [{ id: 123 }, { id: 456 }, { id: 79637 }];
          const mourideCollection: IMouride[] = [{ id: 123 }];
          expectedResult = service.addMourideToCollectionIfMissing(mourideCollection, ...mourideArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const mouride: IMouride = { id: 123 };
          const mouride2: IMouride = { id: 456 };
          expectedResult = service.addMourideToCollectionIfMissing([], mouride, mouride2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mouride);
          expect(expectedResult).toContain(mouride2);
        });

        it('should accept null and undefined values', () => {
          const mouride: IMouride = { id: 123 };
          expectedResult = service.addMourideToCollectionIfMissing([], null, mouride, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mouride);
        });

        it('should return initial array if no Mouride is added', () => {
          const mourideCollection: IMouride[] = [{ id: 123 }];
          expectedResult = service.addMourideToCollectionIfMissing(mourideCollection, undefined, null);
          expect(expectedResult).toEqual(mourideCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
