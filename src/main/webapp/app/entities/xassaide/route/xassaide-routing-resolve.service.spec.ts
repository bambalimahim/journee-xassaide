jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IXassaide, Xassaide } from '../xassaide.model';
import { XassaideService } from '../service/xassaide.service';

import { XassaideRoutingResolveService } from './xassaide-routing-resolve.service';

describe('Service Tests', () => {
  describe('Xassaide routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: XassaideRoutingResolveService;
    let service: XassaideService;
    let resultXassaide: IXassaide | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(XassaideRoutingResolveService);
      service = TestBed.inject(XassaideService);
      resultXassaide = undefined;
    });

    describe('resolve', () => {
      it('should return IXassaide returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultXassaide = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultXassaide).toEqual({ id: 123 });
      });

      it('should return new IXassaide if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultXassaide = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultXassaide).toEqual(new Xassaide());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Xassaide })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultXassaide = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultXassaide).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
