import { Component, Input, OnInit, Type } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { HttpProviderService } from '../Service/http-provider.service';

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

const MODALS: { [name: string]: Type<any> } = {
  deleteModal: NgModalConfirm,
};

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})

export class HomeComponent implements OnInit {
  closeResult = '';
  tourList: any = [];
  constructor(private router: Router, private modalService: NgbModal,
              private toastr: ToastrService, private httpProvider : HttpProviderService) { }

  ngOnInit(): void {
    console.log("HomeComponent initialized");
    this.getAllTour();
  }
  async getAllTour() {
    this.httpProvider.getAllTours().subscribe((data: any) => {
        if (data != null && data.body != null) {
          var resultData = data.body;
          if (resultData) {
            this.tourList = resultData;
            console.log("Tours loaded:", this.tourList); // Fügen Sie diese Zeile hinzu
          }
        }
      },
      (error: any) => {
        console.error("Error loading tours:", error);
      });
  }


  AddTour() {
    this.router.navigate(['AddTour']);
  }

  deleteTourConfirmation(tour: any) {
    // Hier könnten Sie eine Modal-Dialog für die Bestätigung öffnen
    const modalRef = this.modalService.open(NgModalConfirm);
    modalRef.result.then((result) => {
      if (result === 'Ok click') {
        this.deleteTour(tour.id);
      }
    }, (reason) => {
      // Handle dismiss
    });
  }

  deleteTour(tourId: number) {
    this.httpProvider.deleteTourById(tourId).subscribe({
      next: (response) => {
        this.toastr.success('Tour successfully deleted');
        this.getAllTour(); // Aktualisieren der Liste nach dem Löschen
      },
      error: (err) => {
        this.toastr.error('Error deleting tour');
        console.error(err);
      }
    });
  }
}
