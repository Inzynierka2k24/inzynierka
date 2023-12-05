import {inject, Injectable, NgModule} from '@angular/core';
import {CanActivateFn, Router, RouterModule, Routes,} from '@angular/router';
import {WelcomePageComponent} from './welcome-page/welcome-page.component';
import {AddApartmentComponent} from './apartment/add-apartment/add-apartment.component';
import {ApartmentListComponent} from './apartment/apartment-list/apartment-list.component';
import {ReservationCalendarComponent} from './reservation/reservation-calendar/reservation-calendar.component';
import {AddReservationComponent} from './reservation/add-reservation/add-reservation.component';
import {ReservationListComponent} from './reservation/reservation-list/reservation-list.component';
import {EditApartmentComponent} from './apartment/edit-apartment/edit-apartment.component';
import {EditReservationComponent} from './reservation/edit-reservation/edit-reservation.component';
import {FinanceListComponent} from './finance/finance-list/finance-list.component';
import {AddFinanceComponent} from './finance/add-finance/add-finance.component';
import {EditFinanceComponent} from './finance/edit-finance/edit-finance.component';
import {PreferencesComponent} from './user/preferences/preferences.component';
import {selectCurrentUser} from './core/store/user/user.selectors';
import {filter, map, mergeMap, Observable, of, take} from 'rxjs';
import {Store} from '@ngrx/store';
import {DashboardComponent} from './user/dashboard/dashboard.component';
import {LocalStorageService} from './core/services/local-storage.service';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest,} from '@angular/common/http';
import UserActions from './core/store/user/user.actions';
import {MessagingPanelComponent} from './messaging/messaging-panel/messaging-panel.component';
import {FinanceChartComponent} from './finance/finance-chart/finance-chart.component';
import {ExternalOffersComponent} from './apartment/external-offers/external-offers.component';
import {AddExternalOfferComponent} from './apartment/add-external-offer/add-external-offer.component';
import {EditExternalOfferComponent} from './apartment/edit-external-offer/edit-external-offer.component';
import {Actions} from '@ngrx/effects';

/**
 * Appends bearer token to each request when present.
 */
@Injectable()
export class AppInterceptor implements HttpInterceptor {
  constructor(private localStorageService: LocalStorageService) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler,
  ): Observable<HttpEvent<any>> {
    const jwt = this.localStorageService.getData('JWT_TOKEN');

    let request = jwt
      ? req.clone({
          setHeaders: { Authorization: 'Bearer ' + jwt },
        })
      : req.clone();
    return next.handle(request);
  }
}

const authenticatedGuard: CanActivateFn = () => {
  const store = inject(Store);
  const router = inject(Router);
  const localStorageService = inject(LocalStorageService);
  const actions$ = inject(Actions);

  const jwt = localStorageService.getData('JWT_TOKEN');

  return store.select(selectCurrentUser).pipe(
    take(1),
    mergeMap((user) => {
      if (user) {
        return of(true);
      }
      if (!user && jwt) {
        store.dispatch(UserActions.details());
        return actions$.pipe(
          filter((action) => action.type === UserActions.detailsComplete.type),
          take(1),
          map(() => true),
        );
      }
      return of(router.parseUrl(''));
    }),
  );
};

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'user',
        canActivate: [authenticatedGuard],
        children: [
          {
            path: 'settings',
            component: PreferencesComponent,
          },
          {
            path: 'dashboard',
            component: DashboardComponent,
          },
        ],
      },
      {
        path: 'apartments',
        canActivate: [authenticatedGuard],
        children: [
          { path: 'add', component: AddApartmentComponent },
          { path: 'edit', component: EditApartmentComponent },
          { path: 'externalOffers', component: ExternalOffersComponent },
          { path: 'externalOffers/add', component: AddExternalOfferComponent },
          {
            path: 'externalOffers/edit/:externalOfferId/:apartmentId',
            component: EditExternalOfferComponent,
          },
          { path: '', component: ApartmentListComponent },
        ],
      },
      {
        path: 'reservations',
        canActivate: [authenticatedGuard],
        children: [
          { path: 'add', component: AddReservationComponent },
          { path: 'calendar', component: ReservationCalendarComponent },
          { path: 'edit', component: EditReservationComponent },
          { path: '', component: ReservationListComponent },
        ],
      },
      {
        path: 'finances',
        canActivate: [authenticatedGuard],
        children: [
          { path: 'add', component: AddFinanceComponent },
          { path: 'edit', component: EditFinanceComponent },
          { path: 'chart', component: FinanceChartComponent },
          { path: '', component: FinanceListComponent },
        ],
      },
      {
        path: 'messaging',
        canActivate: [authenticatedGuard],
        component: MessagingPanelComponent,
      },
      { path: '**', component: WelcomePageComponent },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  providers: [],
  exports: [RouterModule],
})
export class AppRoutingModule {}
