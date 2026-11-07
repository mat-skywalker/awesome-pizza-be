package org.example.awesomepizzabe.controllers;

import com.matteo.openapi.api.CustomerOrdersApi;
import com.matteo.openapi.model.CreateOrderRequest;
import com.matteo.openapi.model.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.example.awesomepizzabe.services.CustomerOrdersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class CustomerOrdersController implements CustomerOrdersApi {

    private final CustomerOrdersService customerOrdersService;

    @Override
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Validated CreateOrderRequest createOrderRequest) {
        return new ResponseEntity<>(customerOrdersService.createCustomerOrder(createOrderRequest),
                HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/{orderCode}")
    public ResponseEntity<OrderResponse> getOrderByCode(@PathVariable String orderCode) {
        return new ResponseEntity<>(customerOrdersService.getCustomerOrder(orderCode),
                HttpStatus.NOT_IMPLEMENTED);
    }
}
