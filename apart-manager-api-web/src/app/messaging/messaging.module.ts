import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MessagingPanelComponent } from './messaging-panel/messaging-panel.component';
import { DataViewModule } from 'primeng/dataview';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { AvatarModule } from 'primeng/avatar';
import { TabViewModule } from 'primeng/tabview';
import { RatingModule } from 'primeng/rating';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [MessagingPanelComponent],
  imports: [
    CommonModule,
    DataViewModule,
    TagModule,
    ButtonModule,
    AvatarModule,
    TabViewModule,
    RatingModule,
    FormsModule,
  ],
})
export class MessagingModule {}
