import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng/api';
import { Actions } from '@ngrx/effects';
import { UserActionTypes } from './core/store/user/user.store';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [MessageService],
})
export class AppComponent implements OnInit {
  constructor(
    public translate: TranslateService,
    public messageService: MessageService,
    public actions$: Actions,
  ) {
    translate.setDefaultLang('en');
    translate.use('en');
  }

  ngOnInit(): void {
    this.actions$.subscribe((action) => {
      switch (action.type) {
        case UserActionTypes.LOGIN_COMPLETE:
        case UserActionTypes.DETAILS_COMPLETE:
        case UserActionTypes.REGISTER_COMPLETE:
        case UserActionTypes.EDIT_COMPLETE:
          this.messageService.add({
            severity: 'success',
            summary: action.type,
            detail: 'Success',
          });
          break;
        case UserActionTypes.LOGIN_ERROR:
        case UserActionTypes.REGISTER_ERROR:
        case UserActionTypes.DETAILS_ERROR:
        case UserActionTypes.EDIT_ERROR:
          this.messageService.add({
            severity: 'error',
            summary: this.translate.instant('ERROR_MESSAGE.DEFAULT'),
            detail: action.type,
          });
          break;
      }
    });
  }
}
