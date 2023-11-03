import { inject, Injectable, NgModule } from '@angular/core';
import {
  CanActivateFn,
  ResolveFn,
  Router,
  RouterModule,
  Routes,
} from '@angular/router';
import { WelcomePageComponent } from './welcome-page/welcome-page.component';
import { PreferencesComponent } from './user/preferences/preferences.component';
import { selectCurrentUser } from './core/store/user/user.selectors';
import { map, Observable, take } from 'rxjs';
import { Store } from '@ngrx/store';
import { DashboardComponent } from './dashboard/dashboard.component';
import { LocalStorageService } from './core/services/local-storage.service';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import UserActions from './core/store/user/user.actions';

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
import {AddApartmentComponent} from "./header/apartment-widget/add-apartment/add-apartment.component";
import {ApartmentListComponent} from "./header/apartment-widget/apartment-list/apartment-list.component";
import {ReservationListComponent} from "./header/reservation-widget/reservation-list/reservation-list.component";


const routes: Routes = [
  { path: '', component: WelcomePageComponent },
  {path: 'apartments/add', component: AddApartmentComponent},
  {path: 'apartments', component: ApartmentListComponent},
  {path: 'reservations', component: ReservationListComponent},
];
const authenticatedGuard: CanActivateFn = () => {
  const store = inject(Store);
  const router = inject(Router);
  const localStorageService = inject(LocalStorageService);

  const jwt = localStorageService.getData('JWT_TOKEN');

  return store.select(selectCurrentUser).pipe(
    take(1),
    map((user) => {
      if (!user && jwt) {
        store.dispatch(UserActions.details());
        return true;
      }
      return router.parseUrl('');
    }),
  );
};

const authResolver: ResolveFn<boolean> = (route, state) => {
  const store = inject(Store);

  return true;
};
const routes: Routes = [
  {
    path: 'user',
    resolve: authResolver,
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
  { path: '**', component: WelcomePageComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  providers: [],
  exports: [RouterModule],
})
export class AppRoutingModule {}
