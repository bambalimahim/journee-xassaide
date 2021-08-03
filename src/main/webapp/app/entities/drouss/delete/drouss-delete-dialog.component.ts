import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDrouss } from '../drouss.model';
import { DroussService } from '../service/drouss.service';

@Component({
  templateUrl: './drouss-delete-dialog.component.html',
})
export class DroussDeleteDialogComponent {
  drouss?: IDrouss;

  constructor(protected droussService: DroussService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.droussService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
