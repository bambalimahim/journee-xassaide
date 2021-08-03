import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CuisineComponent } from '../list/cuisine.component';
import { CuisineDetailComponent } from '../detail/cuisine-detail.component';
import { CuisineUpdateComponent } from '../update/cuisine-update.component';
import { CuisineRoutingResolveService } from './cuisine-routing-resolve.service';

const cuisineRoute: Routes = [
  {
    path: '',
    component: CuisineComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CuisineDetailComponent,
    resolve: {
      cuisine: CuisineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CuisineUpdateComponent,
    resolve: {
      cuisine: CuisineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CuisineUpdateComponent,
    resolve: {
      cuisine: CuisineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cuisineRoute)],
  exports: [RouterModule],
})
export class CuisineRoutingModule {}
