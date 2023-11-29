import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MessagingPanelComponent } from './messaging-panel/messaging-panel.component';
import { DataViewModule } from 'primeng/dataview';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { AvatarModule } from 'primeng/avatar';
import { TabViewModule } from 'primeng/tabview';
import { RatingModule } from 'primeng/rating';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AddOrderModalComponent } from './add-order-modal/add-order-modal.component';
import { DialogModule } from 'primeng/dialog';
import { DropdownModule } from 'primeng/dropdown';
import { MultiSelectModule } from 'primeng/multiselect';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { AddContactModalComponent } from './add-contact-modal/add-contact-modal.component';

@NgModule({
  declarations: [MessagingPanelComponent, AddOrderModalComponent, AddContactModalComponent],
  imports: [
    CommonModule,
    DataViewModule,
    TagModule,
    ButtonModule,
    AvatarModule,
    TabViewModule,
    RatingModule,
    FormsModule,
    DialogModule,
    DropdownModule,
    MultiSelectModule,
    InputTextModule,
    ReactiveFormsModule,
    InputTextareaModule,
  ],
})
export class MessagingModule {}
