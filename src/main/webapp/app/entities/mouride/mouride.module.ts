import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MourideComponent } from './list/mouride.component';
import { MourideDetailComponent } from './detail/mouride-detail.component';
import { MourideUpdateComponent } from './update/mouride-update.component';
import { MourideDeleteDialogComponent } from './delete/mouride-delete-dialog.component';
import { MourideRoutingModule } from './route/mouride-routing.module';

@NgModule({
  imports: [SharedModule, MourideRoutingModule],
  declarations: [MourideComponent, MourideDetailComponent, MourideUpdateComponent, MourideDeleteDialogComponent],
  entryComponents: [MourideDeleteDialogComponent],
})
export class MourideModule {}
