package com.ug.orderservice.service;

import com.ug.orderservice.model.request.PlaceOrderRequest;
public interface OrderService {

    public void placeOrder(PlaceOrderRequest placeOrderRequest);
}
