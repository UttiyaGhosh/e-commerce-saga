package com.ug.orderservice.controller;

import com.ug.orderservice.model.request.PlaceOrderRequest;
import com.ug.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired private OrderService orderService;

    @PostMapping("/order")
    public void placeOrder(@RequestBody PlaceOrderRequest placeOrderRequest){
        orderService.placeOrder(placeOrderRequest);
    }
}
