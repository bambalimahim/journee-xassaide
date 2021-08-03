import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DroussComponent } from './list/drouss.component';
import { DroussDetailComponent } from './detail/drouss-detail.component';
import { DroussUpdateComponent } from './update/drouss-update.component';
import { DroussDeleteDialogComponent } from './delete/drouss-delete-dialog.component';
import { DroussRoutingModule } from './route/drouss-routing.module';

@NgModule({
  imports: [SharedModule, DroussRoutingModule],
  declarations: [DroussComponent, DroussDetailComponent, DroussUpdateComponent, DroussDeleteDialogComponent],
  entryComponents: [DroussDeleteDialogComponent],
})
export class DroussModule {}
