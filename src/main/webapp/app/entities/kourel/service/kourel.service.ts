import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IKourel, getKourelIdentifier } from '../kourel.model';

export type EntityResponseType = HttpResponse<IKourel>;
export type EntityArrayResponseType = HttpResponse<IKourel[]>;

@Injectable({ providedIn: 'root' })
export class KourelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/kourels');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(kourel: IKourel): Observable<EntityResponseType> {
    return this.http.post<IKourel>(this.resourceUrl, kourel, { observe: 'response' });
  }

  update(kourel: IKourel): Observable<EntityResponseType> {
    return this.http.put<IKourel>(`${this.resourceUrl}/${getKourelIdentifier(kourel) as number}`, kourel, { observe: 'response' });
  }

  partialUpdate(kourel: IKourel): Observable<EntityResponseType> {
    return this.http.patch<IKourel>(`${this.resourceUrl}/${getKourelIdentifier(kourel) as number}`, kourel, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IKourel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IKourel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addKourelToCollectionIfMissing(kourelCollection: IKourel[], ...kourelsToCheck: (IKourel | null | undefined)[]): IKourel[] {
    const kourels: IKourel[] = kourelsToCheck.filter(isPresent);
    if (kourels.length > 0) {
      const kourelCollectionIdentifiers = kourelCollection.map(kourelItem => getKourelIdentifier(kourelItem)!);
      const kourelsToAdd = kourels.filter(kourelItem => {
        const kourelIdentifier = getKourelIdentifier(kourelItem);
        if (kourelIdentifier == null || kourelCollectionIdentifiers.includes(kourelIdentifier)) {
          return false;
        }
        kourelCollectionIdentifiers.push(kourelIdentifier);
        return true;
      });
      return [...kourelsToAdd, ...kourelCollection];
    }
    return kourelCollection;
  }
}
