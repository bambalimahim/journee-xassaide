import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICuisine } from '../cuisine.model';

@Component({
  selector: 'jhi-cuisine-detail',
  templateUrl: './cuisine-detail.component.html',
})
export class CuisineDetailComponent implements OnInit {
  cuisine: ICuisine | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cuisine }) => {
      this.cuisine = cuisine;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
