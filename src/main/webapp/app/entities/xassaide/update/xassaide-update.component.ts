import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IXassaide, Xassaide } from '../xassaide.model';
import { XassaideService } from '../service/xassaide.service';

@Component({
  selector: 'jhi-xassaide-update',
  templateUrl: './xassaide-update.component.html',
})
export class XassaideUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
  });

  constructor(protected xassaideService: XassaideService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ xassaide }) => {
      this.updateForm(xassaide);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const xassaide = this.createFromForm();
    if (xassaide.id !== undefined) {
      this.subscribeToSaveResponse(this.xassaideService.update(xassaide));
    } else {
      this.subscribeToSaveResponse(this.xassaideService.create(xassaide));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IXassaide>>): void {
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

  protected updateForm(xassaide: IXassaide): void {
    this.editForm.patchValue({
      id: xassaide.id,
      nom: xassaide.nom,
    });
  }

  protected createFromForm(): IXassaide {
    return {
      ...new Xassaide(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
    };
  }
}
