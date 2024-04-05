import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToursRoutingModule } from './tours-routing.module';
import { TourCreateComponent } from './components/tour-create/tour-create.component';

@NgModule({
  declarations: [
    TourCreateComponent
    // Fügen Sie hier alle Komponenten, Direktiven und Pipes hinzu, die zu diesem Modul gehören.
  ],
  imports: [
    CommonModule,
    ToursRoutingModule
    // Importieren Sie hier andere Module, die die Komponenten dieses Moduls benötigen.
  ]
  // Keine Notwendigkeit für Provider hier, es sei denn, es gibt spezifische Services nur für dieses Modul.
})
export class ToursModule { }
