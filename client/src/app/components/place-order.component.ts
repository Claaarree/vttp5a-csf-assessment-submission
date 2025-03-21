import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CartStore } from '../cart.store';
import { lastValueFrom, Observable } from 'rxjs';
import { Response, MenuItems, Order, OrderDetails, OrderItems } from '../models';
import { Router } from '@angular/router';
import { RestaurantService } from '../restaurant.service';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent implements OnInit{
  
  // TODO: Task 3
  private fb = inject(FormBuilder);
  private cartStore = inject(CartStore);
  private router = inject(Router);
  private restaurantSvc = inject(RestaurantService);
  form!: FormGroup;
  order$!: Observable<Order>;
  items$!: Observable<MenuItems[]>;
  totalPrice$! : Observable<number>;
  
  ngOnInit(): void {
    this.form = this.createform();
    this.order$ = this.cartStore.getOrder;
    this.items$ = this.cartStore.getItems;
    this.totalPrice$ = this.cartStore.getTotalPrice;
  }

  private createform(): FormGroup {
    return this.form = this.fb.group({
      username: this.fb.control<string>('', Validators.required),
      password: this.fb.control<string>('', Validators.required)
    })
  }

  protected startOver() {
    this.cartStore.setState({
      items: [],
      totalPrice: 0,
      totalQuantity: 0
    });
    this.router.navigate(['']);
  }
  
  protected handleForm() {
    var orderItems: OrderItems[] =[];
    this.items$.subscribe(payload => 
      {
        payload.forEach(i => {
          const item = {
            id: i.id,
            price: i.price,
            quantity: i.quantity
          }
          orderItems.push(item);
        })
      }
    );
    const finalOrder: OrderDetails = {
      username: this.form.value.username,
      password: this.form.value.password,
      items: orderItems
    }
    console.log(finalOrder);
    this.restaurantSvc.sendOrder(finalOrder).then(
      payload => {
        this.router.navigate(['/receipt']);
        // if(payload.message){
        //   console.log(payload)
        //   alert(payload);
        // } else {
        // }
      }
    )
    // .catch<Error>(err => );
  }

}


