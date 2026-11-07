package org.example.awesomepizzabe.controllers;

import com.matteo.openapi.api.KitchenManagementApi;
import com.matteo.openapi.model.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.example.awesomepizzabe.services.KitchenManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/kitchen")
@RequiredArgsConstructor
public class KitchenManagementController implements KitchenManagementApi {

    private final KitchenManagementService kitchenManagementService;

    @Override
    @GetMapping("/orders/pending")
    public ResponseEntity<List<OrderResponse>> getPendingOrders() {
        return new ResponseEntity<>(kitchenManagementService.getPendingOrders(),
                HttpStatus.OK);
    }

    @Override
    @PostMapping("/orders/next")
    public ResponseEntity<OrderResponse> takeNextOrder() {
        return new ResponseEntity<>(kitchenManagementService.getTakeNextOrder(),
                HttpStatus.OK);
    }

    @Override
    @PostMapping("/orders/{orderCode}/complete")
    public ResponseEntity<OrderResponse> completeOrder(@PathVariable String orderCode) {
        return new ResponseEntity<>(kitchenManagementService.getCompleteOrder(orderCode),
                HttpStatus.NOT_IMPLEMENTED);
    }

}
