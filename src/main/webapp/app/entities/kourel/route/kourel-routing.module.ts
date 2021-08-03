import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { KourelComponent } from '../list/kourel.component';
import { KourelDetailComponent } from '../detail/kourel-detail.component';
import { KourelUpdateComponent } from '../update/kourel-update.component';
import { KourelRoutingResolveService } from './kourel-routing-resolve.service';

const kourelRoute: Routes = [
  {
    path: '',
    component: KourelComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: KourelDetailComponent,
    resolve: {
      kourel: KourelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: KourelUpdateComponent,
    resolve: {
      kourel: KourelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: KourelUpdateComponent,
    resolve: {
      kourel: KourelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(kourelRoute)],
  exports: [RouterModule],
})
export class KourelRoutingModule {}
