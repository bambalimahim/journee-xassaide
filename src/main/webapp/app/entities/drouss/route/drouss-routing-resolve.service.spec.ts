jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDrouss, Drouss } from '../drouss.model';
import { DroussService } from '../service/drouss.service';

import { DroussRoutingResolveService } from './drouss-routing-resolve.service';

describe('Service Tests', () => {
  describe('Drouss routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DroussRoutingResolveService;
    let service: DroussService;
    let resultDrouss: IDrouss | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DroussRoutingResolveService);
      service = TestBed.inject(DroussService);
      resultDrouss = undefined;
    });

    describe('resolve', () => {
      it('should return IDrouss returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDrouss = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDrouss).toEqual({ id: 123 });
      });

      it('should return new IDrouss if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDrouss = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDrouss).toEqual(new Drouss());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Drouss })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDrouss = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDrouss).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
