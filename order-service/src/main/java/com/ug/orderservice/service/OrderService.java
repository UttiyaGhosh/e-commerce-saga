package com.ug.orderservice.service;

import com.ug.orderservice.model.request.PlaceOrderRequest;
public interface OrderService {

    void placeOrder(PlaceOrderRequest placeOrderRequest);
}
