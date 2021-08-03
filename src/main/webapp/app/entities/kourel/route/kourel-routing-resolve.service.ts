import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IKourel, Kourel } from '../kourel.model';
import { KourelService } from '../service/kourel.service';

@Injectable({ providedIn: 'root' })
export class KourelRoutingResolveService implements Resolve<IKourel> {
  constructor(protected service: KourelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IKourel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((kourel: HttpResponse<Kourel>) => {
          if (kourel.body) {
            return of(kourel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Kourel());
  }
}
