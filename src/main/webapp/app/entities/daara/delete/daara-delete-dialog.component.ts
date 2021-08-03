import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDaara } from '../daara.model';
import { DaaraService } from '../service/daara.service';

@Component({
  templateUrl: './daara-delete-dialog.component.html',
})
export class DaaraDeleteDialogComponent {
  daara?: IDaara;

  constructor(protected daaraService: DaaraService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.daaraService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
