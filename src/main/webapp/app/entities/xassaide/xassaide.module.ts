import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { XassaideComponent } from './list/xassaide.component';
import { XassaideDetailComponent } from './detail/xassaide-detail.component';
import { XassaideUpdateComponent } from './update/xassaide-update.component';
import { XassaideDeleteDialogComponent } from './delete/xassaide-delete-dialog.component';
import { XassaideRoutingModule } from './route/xassaide-routing.module';

@NgModule({
  imports: [SharedModule, XassaideRoutingModule],
  declarations: [XassaideComponent, XassaideDetailComponent, XassaideUpdateComponent, XassaideDeleteDialogComponent],
  entryComponents: [XassaideDeleteDialogComponent],
})
export class XassaideModule {}
