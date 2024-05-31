import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AddTourComponent } from './Tour/add-tour/add-tour.component';
import { EditTourComponent } from './Tour/edit-tour/edit-tour.component';
import { ViewTourComponent } from './Tour/view-tour/view-tour.component';
import { HttpClientModule } from '@angular/common/http';
import { HomeComponent } from './home/home.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AddTourLogComponent } from './TourLog/add-tour-log/add-tour-log.component';
import { EditTourLogComponent } from './TourLog/edit-tour-log/edit-tour-log.component';
import { TourLogListComponent } from './TourLog/tour-log-list/tour-log-list.component';
import { TourDetailsComponent } from './Tour/tour-details/tour-details.component';
import { StarRatingComponent } from './TourLog/star-rating/star-rating.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    AddTourComponent,
    EditTourComponent,
    ViewTourComponent,
    TourDetailsComponent,
    AddTourLogComponent,
    EditTourLogComponent,
    TourLogListComponent,
    StarRatingComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule, // Hinzufügen des BrowserAnimationsModule
    ToastrModule.forRoot({ // Konfigurieren des ToastrModules
      timeOut: 3000, // Standardzeit, wie lange Toasts angezeigt werden
      positionClass: 'toast-bottom-right',
      preventDuplicates: true,
    }),
    HttpClientModule,
    FormsModule,        // Hinzufügen von FormsModule
    ReactiveFormsModule // Hinzufügen von ReactiveFormsModule, falls benötigt

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
