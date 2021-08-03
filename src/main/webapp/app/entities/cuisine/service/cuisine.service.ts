import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICuisine, getCuisineIdentifier } from '../cuisine.model';

export type EntityResponseType = HttpResponse<ICuisine>;
export type EntityArrayResponseType = HttpResponse<ICuisine[]>;

@Injectable({ providedIn: 'root' })
export class CuisineService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cuisines');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cuisine: ICuisine): Observable<EntityResponseType> {
    return this.http.post<ICuisine>(this.resourceUrl, cuisine, { observe: 'response' });
  }

  update(cuisine: ICuisine): Observable<EntityResponseType> {
    return this.http.put<ICuisine>(`${this.resourceUrl}/${getCuisineIdentifier(cuisine) as number}`, cuisine, { observe: 'response' });
  }

  partialUpdate(cuisine: ICuisine): Observable<EntityResponseType> {
    return this.http.patch<ICuisine>(`${this.resourceUrl}/${getCuisineIdentifier(cuisine) as number}`, cuisine, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICuisine>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICuisine[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCuisineToCollectionIfMissing(cuisineCollection: ICuisine[], ...cuisinesToCheck: (ICuisine | null | undefined)[]): ICuisine[] {
    const cuisines: ICuisine[] = cuisinesToCheck.filter(isPresent);
    if (cuisines.length > 0) {
      const cuisineCollectionIdentifiers = cuisineCollection.map(cuisineItem => getCuisineIdentifier(cuisineItem)!);
      const cuisinesToAdd = cuisines.filter(cuisineItem => {
        const cuisineIdentifier = getCuisineIdentifier(cuisineItem);
        if (cuisineIdentifier == null || cuisineCollectionIdentifiers.includes(cuisineIdentifier)) {
          return false;
        }
        cuisineCollectionIdentifiers.push(cuisineIdentifier);
        return true;
      });
      return [...cuisinesToAdd, ...cuisineCollection];
    }
    return cuisineCollection;
  }
}
