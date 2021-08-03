import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMouride, Mouride } from '../mouride.model';
import { MourideService } from '../service/mouride.service';
import { ICuisine } from 'app/entities/cuisine/cuisine.model';
import { CuisineService } from 'app/entities/cuisine/service/cuisine.service';

@Component({
  selector: 'jhi-mouride-update',
  templateUrl: './mouride-update.component.html',
})
export class MourideUpdateComponent implements OnInit {
  isSaving = false;

  cuisinesSharedCollection: ICuisine[] = [];

  editForm = this.fb.group({
    id: [],
    prenom: [null, [Validators.required]],
    nom: [null, [Validators.required]],
    email: [],
    telephone: [null, [Validators.required]],
    matricule: [null, []],
    cuisine: [],
  });

  constructor(
    protected mourideService: MourideService,
    protected cuisineService: CuisineService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mouride }) => {
      this.updateForm(mouride);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mouride = this.createFromForm();
    if (mouride.id !== undefined) {
      this.subscribeToSaveResponse(this.mourideService.update(mouride));
    } else {
      this.subscribeToSaveResponse(this.mourideService.create(mouride));
    }
  }

  trackCuisineById(index: number, item: ICuisine): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMouride>>): void {
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

  protected updateForm(mouride: IMouride): void {
    this.editForm.patchValue({
      id: mouride.id,
      prenom: mouride.prenom,
      nom: mouride.nom,
      email: mouride.email,
      telephone: mouride.telephone,
      matricule: mouride.matricule,
      cuisine: mouride.cuisine,
    });

    this.cuisinesSharedCollection = this.cuisineService.addCuisineToCollectionIfMissing(this.cuisinesSharedCollection, mouride.cuisine);
  }

  protected loadRelationshipsOptions(): void {
    this.cuisineService
      .query()
      .pipe(map((res: HttpResponse<ICuisine[]>) => res.body ?? []))
      .pipe(
        map((cuisines: ICuisine[]) => this.cuisineService.addCuisineToCollectionIfMissing(cuisines, this.editForm.get('cuisine')!.value))
      )
      .subscribe((cuisines: ICuisine[]) => (this.cuisinesSharedCollection = cuisines));
  }

  protected createFromForm(): IMouride {
    return {
      ...new Mouride(),
      id: this.editForm.get(['id'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      email: this.editForm.get(['email'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      matricule: this.editForm.get(['matricule'])!.value,
      cuisine: this.editForm.get(['cuisine'])!.value,
    };
  }
}
