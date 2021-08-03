import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRegion, Region } from '../region.model';
import { RegionService } from '../service/region.service';
import { ILieu } from 'app/entities/lieu/lieu.model';
import { LieuService } from 'app/entities/lieu/service/lieu.service';

@Component({
  selector: 'jhi-region-update',
  templateUrl: './region-update.component.html',
})
export class RegionUpdateComponent implements OnInit {
  isSaving = false;

  lieusCollection: ILieu[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    lieu: [],
  });

  constructor(
    protected regionService: RegionService,
    protected lieuService: LieuService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ region }) => {
      this.updateForm(region);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const region = this.createFromForm();
    if (region.id !== undefined) {
      this.subscribeToSaveResponse(this.regionService.update(region));
    } else {
      this.subscribeToSaveResponse(this.regionService.create(region));
    }
  }

  trackLieuById(index: number, item: ILieu): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegion>>): void {
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

  protected updateForm(region: IRegion): void {
    this.editForm.patchValue({
      id: region.id,
      nom: region.nom,
      lieu: region.lieu,
    });

    this.lieusCollection = this.lieuService.addLieuToCollectionIfMissing(this.lieusCollection, region.lieu);
  }

  protected loadRelationshipsOptions(): void {
    this.lieuService
      .query({ filter: 'region-is-null' })
      .pipe(map((res: HttpResponse<ILieu[]>) => res.body ?? []))
      .pipe(map((lieus: ILieu[]) => this.lieuService.addLieuToCollectionIfMissing(lieus, this.editForm.get('lieu')!.value)))
      .subscribe((lieus: ILieu[]) => (this.lieusCollection = lieus));
  }

  protected createFromForm(): IRegion {
    return {
      ...new Region(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      lieu: this.editForm.get(['lieu'])!.value,
    };
  }
}
