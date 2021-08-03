import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDaara, Daara } from '../daara.model';
import { DaaraService } from '../service/daara.service';

@Injectable({ providedIn: 'root' })
export class DaaraRoutingResolveService implements Resolve<IDaara> {
  constructor(protected service: DaaraService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDaara> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((daara: HttpResponse<Daara>) => {
          if (daara.body) {
            return of(daara.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Daara());
  }
}
