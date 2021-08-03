import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IKourel, Kourel } from '../kourel.model';
import { KourelService } from '../service/kourel.service';
import { IDaara } from 'app/entities/daara/daara.model';
import { DaaraService } from 'app/entities/daara/service/daara.service';

@Component({
  selector: 'jhi-kourel-update',
  templateUrl: './kourel-update.component.html',
})
export class KourelUpdateComponent implements OnInit {
  isSaving = false;

  daarasSharedCollection: IDaara[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    daara: [],
  });

  constructor(
    protected kourelService: KourelService,
    protected daaraService: DaaraService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kourel }) => {
      this.updateForm(kourel);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const kourel = this.createFromForm();
    if (kourel.id !== undefined) {
      this.subscribeToSaveResponse(this.kourelService.update(kourel));
    } else {
      this.subscribeToSaveResponse(this.kourelService.create(kourel));
    }
  }

  trackDaaraById(index: number, item: IDaara): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKourel>>): void {
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

  protected updateForm(kourel: IKourel): void {
    this.editForm.patchValue({
      id: kourel.id,
      nom: kourel.nom,
      daara: kourel.daara,
    });

    this.daarasSharedCollection = this.daaraService.addDaaraToCollectionIfMissing(this.daarasSharedCollection, kourel.daara);
  }

  protected loadRelationshipsOptions(): void {
    this.daaraService
      .query()
      .pipe(map((res: HttpResponse<IDaara[]>) => res.body ?? []))
      .pipe(map((daaras: IDaara[]) => this.daaraService.addDaaraToCollectionIfMissing(daaras, this.editForm.get('daara')!.value)))
      .subscribe((daaras: IDaara[]) => (this.daarasSharedCollection = daaras));
  }

  protected createFromForm(): IKourel {
    return {
      ...new Kourel(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      daara: this.editForm.get(['daara'])!.value,
    };
  }
}
