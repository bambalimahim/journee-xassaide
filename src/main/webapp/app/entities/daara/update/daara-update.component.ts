import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDaara, Daara } from '../daara.model';
import { DaaraService } from '../service/daara.service';
import { IRegion } from 'app/entities/region/region.model';
import { RegionService } from 'app/entities/region/service/region.service';

@Component({
  selector: 'jhi-daara-update',
  templateUrl: './daara-update.component.html',
})
export class DaaraUpdateComponent implements OnInit {
  isSaving = false;

  regionsSharedCollection: IRegion[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    region: [],
  });

  constructor(
    protected daaraService: DaaraService,
    protected regionService: RegionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ daara }) => {
      this.updateForm(daara);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const daara = this.createFromForm();
    if (daara.id !== undefined) {
      this.subscribeToSaveResponse(this.daaraService.update(daara));
    } else {
      this.subscribeToSaveResponse(this.daaraService.create(daara));
    }
  }

  trackRegionById(index: number, item: IRegion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDaara>>): void {
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

  protected updateForm(daara: IDaara): void {
    this.editForm.patchValue({
      id: daara.id,
      nom: daara.nom,
      region: daara.region,
    });

    this.regionsSharedCollection = this.regionService.addRegionToCollectionIfMissing(this.regionsSharedCollection, daara.region);
  }

  protected loadRelationshipsOptions(): void {
    this.regionService
      .query()
      .pipe(map((res: HttpResponse<IRegion[]>) => res.body ?? []))
      .pipe(map((regions: IRegion[]) => this.regionService.addRegionToCollectionIfMissing(regions, this.editForm.get('region')!.value)))
      .subscribe((regions: IRegion[]) => (this.regionsSharedCollection = regions));
  }

  protected createFromForm(): IDaara {
    return {
      ...new Daara(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      region: this.editForm.get(['region'])!.value,
    };
  }
}
