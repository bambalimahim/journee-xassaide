import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DroussComponent } from '../list/drouss.component';
import { DroussDetailComponent } from '../detail/drouss-detail.component';
import { DroussUpdateComponent } from '../update/drouss-update.component';
import { DroussRoutingResolveService } from './drouss-routing-resolve.service';

const droussRoute: Routes = [
  {
    path: '',
    component: DroussComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DroussDetailComponent,
    resolve: {
      drouss: DroussRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DroussUpdateComponent,
    resolve: {
      drouss: DroussRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DroussUpdateComponent,
    resolve: {
      drouss: DroussRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(droussRoute)],
  exports: [RouterModule],
})
export class DroussRoutingModule {}
