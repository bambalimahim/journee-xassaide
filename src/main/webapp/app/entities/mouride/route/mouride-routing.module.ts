import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MourideComponent } from '../list/mouride.component';
import { MourideDetailComponent } from '../detail/mouride-detail.component';
import { MourideUpdateComponent } from '../update/mouride-update.component';
import { MourideRoutingResolveService } from './mouride-routing-resolve.service';

const mourideRoute: Routes = [
  {
    path: '',
    component: MourideComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MourideDetailComponent,
    resolve: {
      mouride: MourideRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MourideUpdateComponent,
    resolve: {
      mouride: MourideRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MourideUpdateComponent,
    resolve: {
      mouride: MourideRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mourideRoute)],
  exports: [RouterModule],
})
export class MourideRoutingModule {}
