import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IKourel } from '../kourel.model';

@Component({
  selector: 'jhi-kourel-detail',
  templateUrl: './kourel-detail.component.html',
})
export class KourelDetailComponent implements OnInit {
  kourel: IKourel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kourel }) => {
      this.kourel = kourel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
