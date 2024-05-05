import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TourCreateComponent } from './features/tours/components/tour-create/tour-create.component';

const routes: Routes = [
  { path: 'create-tour', component: TourCreateComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
