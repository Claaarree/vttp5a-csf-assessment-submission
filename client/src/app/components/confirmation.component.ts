import { Component, inject, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant.service';
import { Response } from '../models';

@Component({
  selector: 'app-confirmation',
  standalone: false,
  templateUrl: './confirmation.component.html',
  styleUrl: './confirmation.component.css'
})
export class ConfirmationComponent implements OnInit{
  
  // TODO: Task 5
  private restaurantSvc = inject(RestaurantService);
  response!: Response;

  ngOnInit(): void {
    this.response = this.restaurantSvc.response;
  }

}
