// You may use this file to create any models
export interface MenuItems {
    id: string
    name: string
    description: string
    price: number
    quantity: number
}

export interface Order {
    items: MenuItems[]
    totalPrice: number
    totalQuantity: number
}

export interface OrderDetails {
    items: OrderItems[]
    username: string
    password: string
}

export interface OrderItems {
    id: string
    price: number
    quantity: number
}

export interface Response {
    orderId: string
    paymentId: string
    total: number
    timestamp: number
}

export interface Error {
    message: string
}