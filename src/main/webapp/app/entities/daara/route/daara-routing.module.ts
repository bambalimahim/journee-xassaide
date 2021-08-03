import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DaaraComponent } from '../list/daara.component';
import { DaaraDetailComponent } from '../detail/daara-detail.component';
import { DaaraUpdateComponent } from '../update/daara-update.component';
import { DaaraRoutingResolveService } from './daara-routing-resolve.service';

const daaraRoute: Routes = [
  {
    path: '',
    component: DaaraComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DaaraDetailComponent,
    resolve: {
      daara: DaaraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DaaraUpdateComponent,
    resolve: {
      daara: DaaraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DaaraUpdateComponent,
    resolve: {
      daara: DaaraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(daaraRoute)],
  exports: [RouterModule],
})
export class DaaraRoutingModule {}
