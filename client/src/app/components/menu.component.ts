import { Component, inject, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant.service';
import { MenuItems, Order } from '../models';
import { Observable, of } from 'rxjs';
import { CartStore } from '../cart.store';
import { Router } from '@angular/router';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit{
  // TODO: Task 2
  private restaurantSvc = inject(RestaurantService);
  private cartStore = inject(CartStore);
  private router = inject(Router);
  menus!: MenuItems[];
  order: MenuItems[] = [];
  totalPrice$!: Observable<number>;
  totalQuantity$!: Observable<number>;


  ngOnInit(): void {
    this.restaurantSvc.getMenuItems()
      .then(payload => {
        console.log(payload);
        this.menus = payload;
        this.menus.forEach(item => {
          item.quantity = 0;
        })
      }
    );
    this.totalPrice$ = this.cartStore.getTotalPrice;
    this.totalQuantity$ = this.cartStore.getTotalQuantity;
  }

  protected addQuantity(idx: number) {
    const item = this.menus[idx];
    item.quantity += 1;
    this.cartStore.updateOrder(item);
  }
  
  protected minusQuantity(idx: number) {
    const item = this.menus[idx];
    item.quantity -= 1;
    console.log(item.quantity)
    this.cartStore.removeMenuItem(item);
    // if(item.quantity === 0) {
    // } else {
    //   this.cartStore.updateOrder(item);
    // }
  }

  protected goToCheckout() {
    this.router.navigate(['/checkout']);
  }
  
}
