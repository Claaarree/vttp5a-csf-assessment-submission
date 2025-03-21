import { HttpClient } from "@angular/common/http";
import { inject } from "@angular/core";
import { lastValueFrom } from "rxjs";
import { MenuItems, OrderDetails, Response } from "./models";

export class RestaurantService {

  private httpClient = inject(HttpClient);
  response!: Response;
  error!: Error;

  // TODO: Task 2.2
  // You change the method's signature but not the name
  getMenuItems(): Promise<MenuItems[]> {
    return lastValueFrom(this.httpClient.get<MenuItems[]>('/api/menu'));
  }

  // TODO: Task 3.2
  sendOrder(finalOrder: OrderDetails) {
    return lastValueFrom(this.httpClient.post<Response>('/api/food_order', finalOrder)).then(
      payload => this.response = payload
    ).catch<Error>( err => this.error = err);
  }
}
