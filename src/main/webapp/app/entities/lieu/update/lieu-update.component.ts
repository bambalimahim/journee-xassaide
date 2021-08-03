import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILieu, Lieu } from '../lieu.model';
import { LieuService } from '../service/lieu.service';

@Component({
  selector: 'jhi-lieu-update',
  templateUrl: './lieu-update.component.html',
})
export class LieuUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
  });

  constructor(protected lieuService: LieuService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lieu }) => {
      this.updateForm(lieu);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lieu = this.createFromForm();
    if (lieu.id !== undefined) {
      this.subscribeToSaveResponse(this.lieuService.update(lieu));
    } else {
      this.subscribeToSaveResponse(this.lieuService.create(lieu));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILieu>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(lieu: ILieu): void {
    this.editForm.patchValue({
      id: lieu.id,
      nom: lieu.nom,
    });
  }

  protected createFromForm(): ILieu {
    return {
      ...new Lieu(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
    };
  }
}
