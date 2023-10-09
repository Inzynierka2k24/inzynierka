import { Component, OnDestroy, OnInit } from '@angular/core';
import { AppState } from '../../core/store/app.store';
import { Store } from '@ngrx/store';
import {
  selectCurrentUser,
  selectUserStateError,
} from '../../core/store/user/user.selectors';
import { Subscription } from 'rxjs';
import { MessageService } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-user-widget',
  templateUrl: './user-widget.component.html',
  styleUrls: ['./user-widget.component.scss'],
})
export class UserWidgetComponent implements OnInit, OnDestroy {
  subscriptions: Subscription[] = [];

  isUserLoggedIn: boolean = false;
  displayContent: boolean = false;
  formToggle: boolean = false;

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

    this.subscriptions.push(userDataSub, userErrorSub);
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
