import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMouride, getMourideIdentifier } from '../mouride.model';

export type EntityResponseType = HttpResponse<IMouride>;
export type EntityArrayResponseType = HttpResponse<IMouride[]>;

@Injectable({ providedIn: 'root' })
export class MourideService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mourides');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mouride: IMouride): Observable<EntityResponseType> {
    return this.http.post<IMouride>(this.resourceUrl, mouride, { observe: 'response' });
  }

  update(mouride: IMouride): Observable<EntityResponseType> {
    return this.http.put<IMouride>(`${this.resourceUrl}/${getMourideIdentifier(mouride) as number}`, mouride, { observe: 'response' });
  }

  partialUpdate(mouride: IMouride): Observable<EntityResponseType> {
    return this.http.patch<IMouride>(`${this.resourceUrl}/${getMourideIdentifier(mouride) as number}`, mouride, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMouride>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMouride[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMourideToCollectionIfMissing(mourideCollection: IMouride[], ...mouridesToCheck: (IMouride | null | undefined)[]): IMouride[] {
    const mourides: IMouride[] = mouridesToCheck.filter(isPresent);
    if (mourides.length > 0) {
      const mourideCollectionIdentifiers = mourideCollection.map(mourideItem => getMourideIdentifier(mourideItem)!);
      const mouridesToAdd = mourides.filter(mourideItem => {
        const mourideIdentifier = getMourideIdentifier(mourideItem);
        if (mourideIdentifier == null || mourideCollectionIdentifiers.includes(mourideIdentifier)) {
          return false;
        }
        mourideCollectionIdentifiers.push(mourideIdentifier);
        return true;
      });
      return [...mouridesToAdd, ...mourideCollection];
    }
    return mourideCollection;
  }
}
