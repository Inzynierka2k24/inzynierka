import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent} from "@angular/common/http";
import {ExternalOffer, UserDTO} from "../../../generated";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ExternalOfferService {

  constructor(private http: HttpClient) { }

  //todo change endpoints
  getExternalOffers(user: UserDTO): Observable<ExternalOffer[]> {
    return this.http.get<ExternalOffer[]>(`${environment.api_url}/`+ user.id +'/externalOffer');
  }

  addExternalOffer(user: UserDTO, externalOffer: ExternalOffer, options?: any): Observable<HttpEvent<string>> {
    return this.http.post<string>(`${environment.api_url}/`+ user.id +'/externalOffer', externalOffer, options);
  }

  updateExternalOffer(user: UserDTO, externalOffer: ExternalOffer): Observable<ExternalOffer> {
    return this.http.put<ExternalOffer>(`${environment.api_url}/`+ user.id +'/externalOffer/' + externalOffer.id, externalOffer);
  }

  deleteExternalOffer(user: UserDTO, externalOfferId: number, options?: any): Observable<HttpEvent<void>> {
    return this.http.delete<void>(`${environment.api_url}/`+ user.id +'/externalOffer/' + externalOfferId, options);
  }
}
