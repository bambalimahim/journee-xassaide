import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CuisineComponent } from './list/cuisine.component';
import { CuisineDetailComponent } from './detail/cuisine-detail.component';
import { CuisineUpdateComponent } from './update/cuisine-update.component';
import { CuisineDeleteDialogComponent } from './delete/cuisine-delete-dialog.component';
import { CuisineRoutingModule } from './route/cuisine-routing.module';

@NgModule({
  imports: [SharedModule, CuisineRoutingModule],
  declarations: [CuisineComponent, CuisineDetailComponent, CuisineUpdateComponent, CuisineDeleteDialogComponent],
  entryComponents: [CuisineDeleteDialogComponent],
})
export class CuisineModule {}
