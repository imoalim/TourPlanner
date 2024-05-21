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

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    AddTourComponent,
    EditTourComponent,
    ViewTourComponent
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
