import { Component, OnInit, Pipe, PipeTransform } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { HttpProviderService } from '../Service/http-provider.service';
import * as $ from 'jquery'; // Import jQuery
import 'bootstrap'; // Import Bootstrap for tooltips

@Component({
  selector: 'ng-modal-confirm',
  template: `
  <div class="modal-header">
    <h5 class="modal-title" id="modal-title">Delete Confirmation</h5>
    <button type="button" class="btn close" aria-label="Close button" aria-describedby="modal-title" (click)="modal.dismiss('Cross click')">
      <span aria-hidden="true">×</span>
    </button>
  </div>
  <div class="modal-body">
    <p>Are you sure you want to delete?</p>
  </div>
  <div class="modal-footer">
    <button type="button" class="btn btn-outline-secondary" (click)="modal.dismiss('cancel click')">CANCEL</button>
    <button type="button" ngbAutofocus class="btn btn-success" (click)="modal.close('Ok click')">OK</button>
  </div>
  `,
})
export class NgModalConfirm {
  constructor(public modal: NgbActiveModal) { }
}

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  closeResult = '';
  tourList: any = [];
  searchText: string = '';

  constructor(private router: Router, private modalService: NgbModal,
              private toastr: ToastrService, private httpProvider: HttpProviderService) { }

  ngOnInit(): void {
    this.getAllTour();
  }

  getAllTour() {
    this.httpProvider.getAllTours().subscribe((data: any) => {
      if (data != null && data.body != null) {
        this.tourList = data.body;
      }
    },
    (error: any) => {
      console.error("Error loading tours:", error);
    });
  }

  AddTour() {
    this.router.navigate(['AddTour']);
  }

  viewTour(tourId: number) {
    this.router.navigate([`/TourDetails/${tourId}`]);
  }

  editTour(event: Event, tourId: number) {
    event.stopPropagation();
    this.router.navigate([`/EditTour/${tourId}`]);
  }

  deleteTourConfirmation(event: Event, tour: any) {
    event.stopPropagation();
    const modalRef = this.modalService.open(NgModalConfirm);
    modalRef.result.then((result) => {
      if (result === 'Ok click') {
        this.deleteTour(tour.id);
      }
    }, (reason) => {});
  }

  deleteTour(tourId: number) {
    this.httpProvider.deleteTourById(tourId).subscribe({
      next: (response) => {
        this.toastr.success('Tour successfully deleted');
        this.getAllTour();
      },
      error: (err) => {
        this.toastr.error('Error deleting tour');
        console.error(err);
      }
    });
  }

  showTooltip(event: MouseEvent) {
    const element = event.currentTarget as HTMLElement;
    $(element).tooltip({ title: 'Click tour to view more details', placement: 'top', trigger: 'hover' });
    $(element).tooltip('show');
  }

  hideTooltip(event: MouseEvent) {
    const element = event.currentTarget as HTMLElement;
    $(element).tooltip('hide');
  }
}

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {
  transform(items: any[], searchText: string): any[] {
    if (!items) return [];
    if (!searchText) return items;
    searchText = searchText.toLowerCase();
    return items.filter(it => {
      return it.name.toLowerCase().includes(searchText);
    });
  }
}
