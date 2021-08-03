import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDrouss, Drouss } from '../drouss.model';
import { DroussService } from '../service/drouss.service';

@Injectable({ providedIn: 'root' })
export class DroussRoutingResolveService implements Resolve<IDrouss> {
  constructor(protected service: DroussService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDrouss> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((drouss: HttpResponse<Drouss>) => {
          if (drouss.body) {
            return of(drouss.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Drouss());
  }
}
