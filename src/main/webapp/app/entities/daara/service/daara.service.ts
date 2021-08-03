import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDaara, getDaaraIdentifier } from '../daara.model';

export type EntityResponseType = HttpResponse<IDaara>;
export type EntityArrayResponseType = HttpResponse<IDaara[]>;

@Injectable({ providedIn: 'root' })
export class DaaraService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/daaras');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(daara: IDaara): Observable<EntityResponseType> {
    return this.http.post<IDaara>(this.resourceUrl, daara, { observe: 'response' });
  }

  update(daara: IDaara): Observable<EntityResponseType> {
    return this.http.put<IDaara>(`${this.resourceUrl}/${getDaaraIdentifier(daara) as number}`, daara, { observe: 'response' });
  }

  partialUpdate(daara: IDaara): Observable<EntityResponseType> {
    return this.http.patch<IDaara>(`${this.resourceUrl}/${getDaaraIdentifier(daara) as number}`, daara, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDaara>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDaara[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDaaraToCollectionIfMissing(daaraCollection: IDaara[], ...daarasToCheck: (IDaara | null | undefined)[]): IDaara[] {
    const daaras: IDaara[] = daarasToCheck.filter(isPresent);
    if (daaras.length > 0) {
      const daaraCollectionIdentifiers = daaraCollection.map(daaraItem => getDaaraIdentifier(daaraItem)!);
      const daarasToAdd = daaras.filter(daaraItem => {
        const daaraIdentifier = getDaaraIdentifier(daaraItem);
        if (daaraIdentifier == null || daaraCollectionIdentifiers.includes(daaraIdentifier)) {
          return false;
        }
        daaraCollectionIdentifiers.push(daaraIdentifier);
        return true;
      });
      return [...daarasToAdd, ...daaraCollection];
    }
    return daaraCollection;
  }
}
