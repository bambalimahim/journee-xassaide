import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IXassaide, Xassaide } from '../xassaide.model';
import { XassaideService } from '../service/xassaide.service';

@Injectable({ providedIn: 'root' })
export class XassaideRoutingResolveService implements Resolve<IXassaide> {
  constructor(protected service: XassaideService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IXassaide> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((xassaide: HttpResponse<Xassaide>) => {
          if (xassaide.body) {
            return of(xassaide.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Xassaide());
  }
}
