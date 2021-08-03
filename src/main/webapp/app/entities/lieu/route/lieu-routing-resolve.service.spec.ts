jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILieu, Lieu } from '../lieu.model';
import { LieuService } from '../service/lieu.service';

import { LieuRoutingResolveService } from './lieu-routing-resolve.service';

describe('Service Tests', () => {
  describe('Lieu routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LieuRoutingResolveService;
    let service: LieuService;
    let resultLieu: ILieu | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LieuRoutingResolveService);
      service = TestBed.inject(LieuService);
      resultLieu = undefined;
    });

    describe('resolve', () => {
      it('should return ILieu returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLieu = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLieu).toEqual({ id: 123 });
      });

      it('should return new ILieu if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLieu = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLieu).toEqual(new Lieu());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Lieu })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLieu = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLieu).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
