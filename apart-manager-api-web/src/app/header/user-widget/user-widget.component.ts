import { Component, OnDestroy, OnInit } from '@angular/core';
import { AppState } from '../../core/store/app.store';
import { Store } from '@ngrx/store';
import {
  selectCurrentUser,
  selectUserLoadingState,
  selectUserStateError,
} from '../../core/store/user/user.selectors';
import { filter, Subscription } from 'rxjs';
import { MessageService } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';
import { UserDTO } from '../../../generated';
import { Actions, ofType } from '@ngrx/effects';
import UserActions from '../../core/store/user/user.actions';
import { UserActionTypes } from '../../core/store/user/user.store';
import { NavigationStart, Router } from '@angular/router';

@Component({
  selector: 'app-user-widget',
  templateUrl: './user-widget.component.html',
  styleUrls: ['./user-widget.component.scss'],
})
export class UserWidgetComponent implements OnInit, OnDestroy {
  subscriptions: Subscription[] = [];

  user: UserDTO;
  isUserLoggedIn = false;
  displayContent = false;
  formToggle = false;

  constructor(
    private store: Store<AppState>,
    private messageService: MessageService,
    private translateService: TranslateService,
    private actions$: Actions,
    private router: Router,
  ) {}

  ngOnInit(): void {
    const userDataSub = this.store
      .select(selectCurrentUser)
      .subscribe((user) => {
        if (user) {
          this.isUserLoggedIn = true;
          this.user = user;
        } else {
          this.isUserLoggedIn = false;
        }
      });
    const actionErrorNotificationSub = this.store
      .select(selectUserStateError)
      .subscribe((error) => {
        if (error) {
          this.messageService.add({
            severity: 'error',
            summary: this.translateService.instant('ERROR_MESSAGE.DEFAULT'),
            detail: error.type,
          });
        }
      });
    const loadingSpinnerSub = this.store
      .select(selectUserLoadingState)
      .subscribe((loading) => {
        if (!loading) {
          this.displayContent = false;
        }
      });
    const showNotificationOnActionCompleteSub = this.actions$
      .pipe(ofType(UserActions.loginComplete, UserActions.registerComplete))
      .subscribe((action) => {
        if (action.type === UserActionTypes.REGISTER_COMPLETE) {
          this.toggleForm();
        }
        this.messageService.add({
          severity: 'success',
          summary: action.type,
          detail:
            action.type === UserActionTypes.LOGIN_COMPLETE
              ? 'Successfully logged in.'
              : 'Registration Success. Please log in.',
        });
      });
    const hideOnNavigationSub = this.router.events
      .pipe(filter((event) => event instanceof NavigationStart))
      .subscribe(() => {
        this.displayContent = false;
      });

    this.subscriptions.push(
      userDataSub,
      actionErrorNotificationSub,
      loadingSpinnerSub,
      showNotificationOnActionCompleteSub,
      hideOnNavigationSub,
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }

  toggleView() {
    this.displayContent = !this.displayContent;
  }

  toggleForm() {
    this.formToggle = !this.formToggle;
  }
}
