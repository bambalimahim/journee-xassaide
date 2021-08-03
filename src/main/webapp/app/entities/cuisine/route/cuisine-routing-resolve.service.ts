import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICuisine, Cuisine } from '../cuisine.model';
import { CuisineService } from '../service/cuisine.service';

@Injectable({ providedIn: 'root' })
export class CuisineRoutingResolveService implements Resolve<ICuisine> {
  constructor(protected service: CuisineService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICuisine> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cuisine: HttpResponse<Cuisine>) => {
          if (cuisine.body) {
            return of(cuisine.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cuisine());
  }
}
