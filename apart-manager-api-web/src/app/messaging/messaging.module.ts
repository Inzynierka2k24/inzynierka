import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MessagingPanelComponent } from './messaging-panel/messaging-panel.component';
import { DataViewModule } from 'primeng/dataview';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { AvatarModule } from 'primeng/avatar';
import { TabViewModule } from 'primeng/tabview';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AddOrderModalComponent } from './add-order-modal/add-order-modal.component';
import { DialogModule } from 'primeng/dialog';
import { DropdownModule } from 'primeng/dropdown';
import { MultiSelectModule } from 'primeng/multiselect';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { AddContactModalComponent } from './add-contact-modal/add-contact-modal.component';
import { TableModule } from 'primeng/table';
import { CalendarModule } from 'primeng/calendar';
import { FullCalendarModule } from '@fullcalendar/angular';
import { ApartmentCalendarViewComponent } from './apartment-calendar-view/apartment-calendar-view.component';
import { SpinnerModule } from 'primeng/spinner';
import { ProgressSpinnerModule } from 'primeng/progressspinner';

@NgModule({
  declarations: [
    MessagingPanelComponent,
    AddOrderModalComponent,
    AddContactModalComponent,
    ApartmentCalendarViewComponent,
  ],
  imports: [
    CommonModule,
    DataViewModule,
    TagModule,
    ButtonModule,
    AvatarModule,
    TabViewModule,
    FormsModule,
    DialogModule,
    DropdownModule,
    MultiSelectModule,
    InputTextModule,
    ReactiveFormsModule,
    InputTextareaModule,
    TableModule,
    CalendarModule,
    FullCalendarModule,
    SpinnerModule,
    ProgressSpinnerModule,
  ],
})
export class MessagingModule {}
