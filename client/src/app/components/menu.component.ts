import { Component, inject, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant.service';
import { MenuItems } from '../models';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit{
  // TODO: Task 2
  private restaurantSvc = inject(RestaurantService);
  menus!: MenuItems[];

  ngOnInit(): void {
    this.restaurantSvc.getMenuItems()
      .then(payload => {
        console.log(payload);
        this.menus = payload;
      }
    )
  }
  
}
