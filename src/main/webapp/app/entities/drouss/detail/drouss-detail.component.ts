import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDrouss } from '../drouss.model';

@Component({
  selector: 'jhi-drouss-detail',
  templateUrl: './drouss-detail.component.html',
})
export class DroussDetailComponent implements OnInit {
  drouss: IDrouss | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ drouss }) => {
      this.drouss = drouss;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
