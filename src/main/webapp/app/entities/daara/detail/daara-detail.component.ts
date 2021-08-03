import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDaara } from '../daara.model';

@Component({
  selector: 'jhi-daara-detail',
  templateUrl: './daara-detail.component.html',
})
export class DaaraDetailComponent implements OnInit {
  daara: IDaara | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ daara }) => {
      this.daara = daara;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
