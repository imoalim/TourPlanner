import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AddTourComponent } from './Tour/add-tour/add-tour.component';
import { ViewTourComponent } from './Tour/view-tour/view-tour.component';
import { EditTourComponent } from './Tour/edit-tour/edit-tour.component';
import { TourDetailsComponent } from './Tour/tour-details/tour-details.component';
import { AddTourLogComponent } from './TourLog/add-tour-log/add-tour-log.component';
import { EditTourLogComponent} from './TourLog/edit-tour-log/edit-tour-log.component';

const routes: Routes = [
  { path: '', redirectTo: 'Home', pathMatch: 'full'},
  { path: 'Home', component: HomeComponent },
  { path: 'ViewTour/:tourId', component: ViewTourComponent },
  { path: 'AddTour', component: AddTourComponent },
  { path: 'EditTour/:TourId', component: EditTourComponent },
  { path: 'TourDetails/:tourId', component: TourDetailsComponent },
  { path: 'AddTourLog/:id', component: AddTourLogComponent },
  { path: 'EditTourLog/:logId', component: EditTourLogComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
