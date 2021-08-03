jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICuisine, Cuisine } from '../cuisine.model';
import { CuisineService } from '../service/cuisine.service';

import { CuisineRoutingResolveService } from './cuisine-routing-resolve.service';

describe('Service Tests', () => {
  describe('Cuisine routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CuisineRoutingResolveService;
    let service: CuisineService;
    let resultCuisine: ICuisine | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CuisineRoutingResolveService);
      service = TestBed.inject(CuisineService);
      resultCuisine = undefined;
    });

    describe('resolve', () => {
      it('should return ICuisine returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCuisine = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCuisine).toEqual({ id: 123 });
      });

      it('should return new ICuisine if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCuisine = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCuisine).toEqual(new Cuisine());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Cuisine })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCuisine = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCuisine).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
