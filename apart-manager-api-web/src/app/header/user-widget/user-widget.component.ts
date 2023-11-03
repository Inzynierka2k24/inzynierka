import { Component, OnDestroy, OnInit } from '@angular/core';
import { AppState } from '../../core/store/app.store';
import { Store } from '@ngrx/store';
import {
  selectCurrentUser,
  selectUserLoadingState,
  selectUserStateError,
} from '../../core/store/user/user.selectors';
import { Subscription } from 'rxjs';
import { MessageService } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';
import { UserDTO } from '../../../generated';

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
    const userErrorSub = this.store
      .select(selectUserStateError)
      .subscribe((error) => {
        if (error) {
          switch (error.status) {
            default:
              console.warn(error);
              this.messageService.add({
                severity: 'error',
                summary: this.translateService.instant('ERROR_MESSAGE.DEFAULT'),
                detail: error.type,
              });
          }
        }
      });
    const loadingSub = this.store
      .select(selectUserLoadingState)
      .subscribe((loading) => {
        if (!loading) {
          this.displayContent = false;
        }
      });

    this.subscriptions.push(userDataSub, userErrorSub, loadingSub);
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
