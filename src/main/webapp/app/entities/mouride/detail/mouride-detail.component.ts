import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMouride } from '../mouride.model';

@Component({
  selector: 'jhi-mouride-detail',
  templateUrl: './mouride-detail.component.html',
})
export class MourideDetailComponent implements OnInit {
  mouride: IMouride | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mouride }) => {
      this.mouride = mouride;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
