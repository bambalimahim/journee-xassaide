import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IKourel } from '../kourel.model';
import { KourelService } from '../service/kourel.service';

@Component({
  templateUrl: './kourel-delete-dialog.component.html',
})
export class KourelDeleteDialogComponent {
  kourel?: IKourel;

  constructor(protected kourelService: KourelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.kourelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
