import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IXassaide } from '../xassaide.model';

@Component({
  selector: 'jhi-xassaide-detail',
  templateUrl: './xassaide-detail.component.html',
})
export class XassaideDetailComponent implements OnInit {
  xassaide: IXassaide | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ xassaide }) => {
      this.xassaide = xassaide;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
