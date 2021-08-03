import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDrouss, Drouss } from '../drouss.model';
import { DroussService } from '../service/drouss.service';
import { IXassaide } from 'app/entities/xassaide/xassaide.model';
import { XassaideService } from 'app/entities/xassaide/service/xassaide.service';
import { ILieu } from 'app/entities/lieu/lieu.model';
import { LieuService } from 'app/entities/lieu/service/lieu.service';

@Component({
  selector: 'jhi-drouss-update',
  templateUrl: './drouss-update.component.html',
})
export class DroussUpdateComponent implements OnInit {
  isSaving = false;

  xassaidesSharedCollection: IXassaide[] = [];
  lieusSharedCollection: ILieu[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    xassaide: [],
    lieu: [],
  });

  constructor(
    protected droussService: DroussService,
    protected xassaideService: XassaideService,
    protected lieuService: LieuService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ drouss }) => {
      this.updateForm(drouss);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const drouss = this.createFromForm();
    if (drouss.id !== undefined) {
      this.subscribeToSaveResponse(this.droussService.update(drouss));
    } else {
      this.subscribeToSaveResponse(this.droussService.create(drouss));
    }
  }

  trackXassaideById(index: number, item: IXassaide): number {
    return item.id!;
  }

  trackLieuById(index: number, item: ILieu): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDrouss>>): void {
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

  protected updateForm(drouss: IDrouss): void {
    this.editForm.patchValue({
      id: drouss.id,
      nombre: drouss.nombre,
      xassaide: drouss.xassaide,
      lieu: drouss.lieu,
    });

    this.xassaidesSharedCollection = this.xassaideService.addXassaideToCollectionIfMissing(this.xassaidesSharedCollection, drouss.xassaide);
    this.lieusSharedCollection = this.lieuService.addLieuToCollectionIfMissing(this.lieusSharedCollection, drouss.lieu);
  }

  protected loadRelationshipsOptions(): void {
    this.xassaideService
      .query()
      .pipe(map((res: HttpResponse<IXassaide[]>) => res.body ?? []))
      .pipe(
        map((xassaides: IXassaide[]) =>
          this.xassaideService.addXassaideToCollectionIfMissing(xassaides, this.editForm.get('xassaide')!.value)
        )
      )
      .subscribe((xassaides: IXassaide[]) => (this.xassaidesSharedCollection = xassaides));

    this.lieuService
      .query()
      .pipe(map((res: HttpResponse<ILieu[]>) => res.body ?? []))
      .pipe(map((lieus: ILieu[]) => this.lieuService.addLieuToCollectionIfMissing(lieus, this.editForm.get('lieu')!.value)))
      .subscribe((lieus: ILieu[]) => (this.lieusSharedCollection = lieus));
  }

  protected createFromForm(): IDrouss {
    return {
      ...new Drouss(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      xassaide: this.editForm.get(['xassaide'])!.value,
      lieu: this.editForm.get(['lieu'])!.value,
    };
  }
}
