import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMouride } from '../mouride.model';
import { MourideService } from '../service/mouride.service';

@Component({
  templateUrl: './mouride-delete-dialog.component.html',
})
export class MourideDeleteDialogComponent {
  mouride?: IMouride;

  constructor(protected mourideService: MourideService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mourideService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
