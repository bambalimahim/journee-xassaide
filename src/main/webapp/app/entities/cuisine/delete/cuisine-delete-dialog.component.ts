import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICuisine } from '../cuisine.model';
import { CuisineService } from '../service/cuisine.service';

@Component({
  templateUrl: './cuisine-delete-dialog.component.html',
})
export class CuisineDeleteDialogComponent {
  cuisine?: ICuisine;

  constructor(protected cuisineService: CuisineService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cuisineService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
