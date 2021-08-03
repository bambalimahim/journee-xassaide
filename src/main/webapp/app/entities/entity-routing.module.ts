import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'region',
        data: { pageTitle: 'journeeXassaideApp.region.home.title' },
        loadChildren: () => import('./region/region.module').then(m => m.RegionModule),
      },
      {
        path: 'lieu',
        data: { pageTitle: 'journeeXassaideApp.lieu.home.title' },
        loadChildren: () => import('./lieu/lieu.module').then(m => m.LieuModule),
      },
      {
        path: 'kourel',
        data: { pageTitle: 'journeeXassaideApp.kourel.home.title' },
        loadChildren: () => import('./kourel/kourel.module').then(m => m.KourelModule),
      },
      {
        path: 'cuisine',
        data: { pageTitle: 'journeeXassaideApp.cuisine.home.title' },
        loadChildren: () => import('./cuisine/cuisine.module').then(m => m.CuisineModule),
      },
      {
        path: 'xassaide',
        data: { pageTitle: 'journeeXassaideApp.xassaide.home.title' },
        loadChildren: () => import('./xassaide/xassaide.module').then(m => m.XassaideModule),
      },
      {
        path: 'daara',
        data: { pageTitle: 'journeeXassaideApp.daara.home.title' },
        loadChildren: () => import('./daara/daara.module').then(m => m.DaaraModule),
      },
      {
        path: 'mouride',
        data: { pageTitle: 'journeeXassaideApp.mouride.home.title' },
        loadChildren: () => import('./mouride/mouride.module').then(m => m.MourideModule),
      },
      {
        path: 'drouss',
        data: { pageTitle: 'journeeXassaideApp.drouss.home.title' },
        loadChildren: () => import('./drouss/drouss.module').then(m => m.DroussModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
