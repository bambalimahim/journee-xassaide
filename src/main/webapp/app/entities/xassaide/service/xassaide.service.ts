import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IXassaide, getXassaideIdentifier } from '../xassaide.model';

export type EntityResponseType = HttpResponse<IXassaide>;
export type EntityArrayResponseType = HttpResponse<IXassaide[]>;

@Injectable({ providedIn: 'root' })
export class XassaideService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/xassaides');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(xassaide: IXassaide): Observable<EntityResponseType> {
    return this.http.post<IXassaide>(this.resourceUrl, xassaide, { observe: 'response' });
  }

  update(xassaide: IXassaide): Observable<EntityResponseType> {
    return this.http.put<IXassaide>(`${this.resourceUrl}/${getXassaideIdentifier(xassaide) as number}`, xassaide, { observe: 'response' });
  }

  partialUpdate(xassaide: IXassaide): Observable<EntityResponseType> {
    return this.http.patch<IXassaide>(`${this.resourceUrl}/${getXassaideIdentifier(xassaide) as number}`, xassaide, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IXassaide>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IXassaide[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addXassaideToCollectionIfMissing(xassaideCollection: IXassaide[], ...xassaidesToCheck: (IXassaide | null | undefined)[]): IXassaide[] {
    const xassaides: IXassaide[] = xassaidesToCheck.filter(isPresent);
    if (xassaides.length > 0) {
      const xassaideCollectionIdentifiers = xassaideCollection.map(xassaideItem => getXassaideIdentifier(xassaideItem)!);
      const xassaidesToAdd = xassaides.filter(xassaideItem => {
        const xassaideIdentifier = getXassaideIdentifier(xassaideItem);
        if (xassaideIdentifier == null || xassaideCollectionIdentifiers.includes(xassaideIdentifier)) {
          return false;
        }
        xassaideCollectionIdentifiers.push(xassaideIdentifier);
        return true;
      });
      return [...xassaidesToAdd, ...xassaideCollection];
    }
    return xassaideCollection;
  }
}
