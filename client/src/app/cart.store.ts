import { ComponentStore } from "@ngrx/component-store";
import { MenuItems, Order } from "./models";
import { Injectable } from "@angular/core";
import { iterator } from "rxjs/internal/symbol/iterator";

const INIT_STATE = {
    items: [],
    totalPrice: 0,
    totalQuantity: 0
}

@Injectable()
export class CartStore extends ComponentStore<Order> {
    
    constructor() {
        super(INIT_STATE);
    }

    readonly getOrder = this.select<Order>(
        (state: Order) => state
    )

    readonly getItems = this.select<MenuItems[]>(
        (state: Order) => state.items
    );

    readonly getTotalPrice = this.select<number>(
        (state: Order) => state.totalPrice
    );

    readonly getTotalQuantity = this.select<number>(
        (state: Order) => state.totalQuantity
    );

    readonly updateOrder = this.updater<MenuItems>(
        (state: Order, newItem: MenuItems) => {
            const foundItem = state.items.find(i => i.id === newItem.id)
            if(foundItem) {
            //     console.log("new item qtd", newItem.quantity)
            //     console.log("found item qtd", foundItem.quantity)
    
            //     const quantityToAdd = newItem.quantity - foundItem.quantity;
            //     console.log("qtd", quantityToAdd)
            //     const priceToAdd = quantityToAdd * newItem.price;
            //     console.log("price", priceToAdd)
            //     const removedItem = state.items.filter(i => i.id !== newItem.id);
            //     console.log(removedItem);

            //    const newOrder = {
            //     items: [...removedItem, newItem],
            //     totalPrice: state.totalPrice + priceToAdd,
            //     totalQuantity: state.totalQuantity + quantityToAdd
            // };
            //     return newOrder;
            // }
            // var totalPrice: number = 0;
            // var totalQuantity: number = 0;
            // removedItem.forEach(j => {
            //     totalPrice += j.price;
            //     totalQuantity =+ j.quantity
            // });
                const newOrder = {
                    items: [...state.items],
                    totalPrice: state.totalPrice + newItem.price,
                    totalQuantity: state.totalQuantity + 1
                };
                return newOrder;
            }
            const newOrder = {
                items: [...state.items, newItem],
                totalPrice: state.totalPrice + newItem.price,
                totalQuantity: state.totalQuantity + 1
            };
            return newOrder;
        }
    );

    readonly removeMenuItem = this.updater<MenuItems>(
        (state: Order, item: MenuItems) => {
            const newCart = {
                items: state.items.filter(item => item.id !== item.id),
                totalPrice: state.totalPrice - item.price,
                totalQuantity: state.totalQuantity - 1
            }
            return newCart;
        }
    );
    
}