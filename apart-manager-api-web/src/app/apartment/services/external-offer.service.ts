import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent} from "@angular/common/http";
import {Apartment, ExternalOffer, UserDTO} from "../../../generated";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ExternalOfferService {

  constructor(private http: HttpClient) { }

  getExternalOffers(user: UserDTO, apartment: Apartment): Observable<ExternalOffer[]> {
    return this.http.get<ExternalOffer[]>(
      `${environment.api_url}/`+ user.id +'/apartment/' + apartment.id +'/external/offer'
    );
  }

  addExternalOffer(user: UserDTO, apartment: Apartment, externalOffer: ExternalOffer, options?: any): Observable<HttpEvent<string>> {
    return this.http.post<string>(
      `${environment.api_url}/`+ user.id + '/apartment/' + apartment.id +'/external/offer', externalOffer, options
    );
  }

  updateExternalOffer(user: UserDTO, apartment: Apartment, externalOffer: ExternalOffer, options?: any):
    Observable<HttpEvent<ExternalOffer>> {
    return this.http.put<ExternalOffer>(
      `${environment.api_url}/`+ user.id + '/apartment/' + apartment.id + '/external/offer', externalOffer, options
    );
  }

  deleteExternalOffer(user: UserDTO, apartment: Apartment, externalOffer: ExternalOffer, options?: any): Observable<HttpEvent<void>> {
    return this.http.delete<void>(
      `${environment.api_url}/`+ user.id + '/apartment/' + apartment.id +'/external/offer' + externalOffer.id, options
    );
  }
}
