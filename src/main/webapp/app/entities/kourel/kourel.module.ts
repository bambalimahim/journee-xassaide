import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { KourelComponent } from './list/kourel.component';
import { KourelDetailComponent } from './detail/kourel-detail.component';
import { KourelUpdateComponent } from './update/kourel-update.component';
import { KourelDeleteDialogComponent } from './delete/kourel-delete-dialog.component';
import { KourelRoutingModule } from './route/kourel-routing.module';

@NgModule({
  imports: [SharedModule, KourelRoutingModule],
  declarations: [KourelComponent, KourelDetailComponent, KourelUpdateComponent, KourelDeleteDialogComponent],
  entryComponents: [KourelDeleteDialogComponent],
})
export class KourelModule {}
