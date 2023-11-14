import { Component, OnDestroy, OnInit } from '@angular/core';
import { AppState } from '../../core/store/app.store';
import { Store } from '@ngrx/store';
import {
  selectCurrentUser,
  selectUserLoadingState,
} from '../../core/store/user/user.selectors';
import { filter, Subscription } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { UserDTO } from '../../../generated';
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
    private translateService: TranslateService,
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
    const loadingSpinnerSub = this.store
      .select(selectUserLoadingState)
      .subscribe((loading) => {
        if (!loading) {
          this.displayContent = false;
        }
      });
    const hideOnNavigationSub = this.router.events
      .pipe(filter((event) => event instanceof NavigationStart))
      .subscribe(() => {
        this.displayContent = false;
      });

    this.subscriptions.push(
      userDataSub,
      loadingSpinnerSub,
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
