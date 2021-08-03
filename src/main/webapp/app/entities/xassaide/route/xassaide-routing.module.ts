import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { XassaideComponent } from '../list/xassaide.component';
import { XassaideDetailComponent } from '../detail/xassaide-detail.component';
import { XassaideUpdateComponent } from '../update/xassaide-update.component';
import { XassaideRoutingResolveService } from './xassaide-routing-resolve.service';

const xassaideRoute: Routes = [
  {
    path: '',
    component: XassaideComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: XassaideDetailComponent,
    resolve: {
      xassaide: XassaideRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: XassaideUpdateComponent,
    resolve: {
      xassaide: XassaideRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: XassaideUpdateComponent,
    resolve: {
      xassaide: XassaideRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(xassaideRoute)],
  exports: [RouterModule],
})
export class XassaideRoutingModule {}
