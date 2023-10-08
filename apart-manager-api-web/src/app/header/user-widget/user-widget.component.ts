import { Component, OnDestroy, OnInit } from '@angular/core';
import { AppState } from '../../core/store/app.store';
import { Store } from '@ngrx/store';
import { selectCurrentUser } from '../../core/store/user/user.selectors';
import { Subscription } from 'rxjs';

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

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    const userDataSub = this.store
      .select(selectCurrentUser)
      .subscribe((user) => {
        if (user) {
          this.isUserLoggedIn = true;
        }
      });
    this.subscriptions.push(userDataSub);
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
