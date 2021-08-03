import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DaaraComponent } from './list/daara.component';
import { DaaraDetailComponent } from './detail/daara-detail.component';
import { DaaraUpdateComponent } from './update/daara-update.component';
import { DaaraDeleteDialogComponent } from './delete/daara-delete-dialog.component';
import { DaaraRoutingModule } from './route/daara-routing.module';

@NgModule({
  imports: [SharedModule, DaaraRoutingModule],
  declarations: [DaaraComponent, DaaraDetailComponent, DaaraUpdateComponent, DaaraDeleteDialogComponent],
  entryComponents: [DaaraDeleteDialogComponent],
})
export class DaaraModule {}
