import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDrouss, getDroussIdentifier } from '../drouss.model';

export type EntityResponseType = HttpResponse<IDrouss>;
export type EntityArrayResponseType = HttpResponse<IDrouss[]>;

@Injectable({ providedIn: 'root' })
export class DroussService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/drousses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(drouss: IDrouss): Observable<EntityResponseType> {
    return this.http.post<IDrouss>(this.resourceUrl, drouss, { observe: 'response' });
  }

  update(drouss: IDrouss): Observable<EntityResponseType> {
    return this.http.put<IDrouss>(`${this.resourceUrl}/${getDroussIdentifier(drouss) as number}`, drouss, { observe: 'response' });
  }

  partialUpdate(drouss: IDrouss): Observable<EntityResponseType> {
    return this.http.patch<IDrouss>(`${this.resourceUrl}/${getDroussIdentifier(drouss) as number}`, drouss, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDrouss>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDrouss[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDroussToCollectionIfMissing(droussCollection: IDrouss[], ...droussesToCheck: (IDrouss | null | undefined)[]): IDrouss[] {
    const drousses: IDrouss[] = droussesToCheck.filter(isPresent);
    if (drousses.length > 0) {
      const droussCollectionIdentifiers = droussCollection.map(droussItem => getDroussIdentifier(droussItem)!);
      const droussesToAdd = drousses.filter(droussItem => {
        const droussIdentifier = getDroussIdentifier(droussItem);
        if (droussIdentifier == null || droussCollectionIdentifiers.includes(droussIdentifier)) {
          return false;
        }
        droussCollectionIdentifiers.push(droussIdentifier);
        return true;
      });
      return [...droussesToAdd, ...droussCollection];
    }
    return droussCollection;
  }
}
