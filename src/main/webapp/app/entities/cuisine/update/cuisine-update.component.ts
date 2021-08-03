import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICuisine, Cuisine } from '../cuisine.model';
import { CuisineService } from '../service/cuisine.service';
import { ILieu } from 'app/entities/lieu/lieu.model';
import { LieuService } from 'app/entities/lieu/service/lieu.service';

@Component({
  selector: 'jhi-cuisine-update',
  templateUrl: './cuisine-update.component.html',
})
export class CuisineUpdateComponent implements OnInit {
  isSaving = false;

  lieusCollection: ILieu[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    capacite: [],
    lieu: [],
  });

  constructor(
    protected cuisineService: CuisineService,
    protected lieuService: LieuService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cuisine }) => {
      this.updateForm(cuisine);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cuisine = this.createFromForm();
    if (cuisine.id !== undefined) {
      this.subscribeToSaveResponse(this.cuisineService.update(cuisine));
    } else {
      this.subscribeToSaveResponse(this.cuisineService.create(cuisine));
    }
  }

  trackLieuById(index: number, item: ILieu): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICuisine>>): void {
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

  protected updateForm(cuisine: ICuisine): void {
    this.editForm.patchValue({
      id: cuisine.id,
      nom: cuisine.nom,
      capacite: cuisine.capacite,
      lieu: cuisine.lieu,
    });

    this.lieusCollection = this.lieuService.addLieuToCollectionIfMissing(this.lieusCollection, cuisine.lieu);
  }

  protected loadRelationshipsOptions(): void {
    this.lieuService
      .query({ filter: 'cuisine-is-null' })
      .pipe(map((res: HttpResponse<ILieu[]>) => res.body ?? []))
      .pipe(map((lieus: ILieu[]) => this.lieuService.addLieuToCollectionIfMissing(lieus, this.editForm.get('lieu')!.value)))
      .subscribe((lieus: ILieu[]) => (this.lieusCollection = lieus));
  }

  protected createFromForm(): ICuisine {
    return {
      ...new Cuisine(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      capacite: this.editForm.get(['capacite'])!.value,
      lieu: this.editForm.get(['lieu'])!.value,
    };
  }
}
