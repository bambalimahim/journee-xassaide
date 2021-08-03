import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IXassaide } from '../xassaide.model';
import { XassaideService } from '../service/xassaide.service';

@Component({
  templateUrl: './xassaide-delete-dialog.component.html',
})
export class XassaideDeleteDialogComponent {
  xassaide?: IXassaide;

  constructor(protected xassaideService: XassaideService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.xassaideService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
