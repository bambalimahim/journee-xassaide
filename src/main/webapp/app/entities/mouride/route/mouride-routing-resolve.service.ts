import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMouride, Mouride } from '../mouride.model';
import { MourideService } from '../service/mouride.service';

@Injectable({ providedIn: 'root' })
export class MourideRoutingResolveService implements Resolve<IMouride> {
  constructor(protected service: MourideService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMouride> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mouride: HttpResponse<Mouride>) => {
          if (mouride.body) {
            return of(mouride.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Mouride());
  }
}
