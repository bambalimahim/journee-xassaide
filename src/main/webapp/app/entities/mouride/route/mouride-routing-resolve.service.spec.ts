jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMouride, Mouride } from '../mouride.model';
import { MourideService } from '../service/mouride.service';

import { MourideRoutingResolveService } from './mouride-routing-resolve.service';

describe('Service Tests', () => {
  describe('Mouride routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MourideRoutingResolveService;
    let service: MourideService;
    let resultMouride: IMouride | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MourideRoutingResolveService);
      service = TestBed.inject(MourideService);
      resultMouride = undefined;
    });

    describe('resolve', () => {
      it('should return IMouride returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMouride = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMouride).toEqual({ id: 123 });
      });

      it('should return new IMouride if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMouride = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMouride).toEqual(new Mouride());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Mouride })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMouride = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMouride).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
