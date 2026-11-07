package org.example.awesomepizzabe.controllers;

import com.matteo.openapi.model.OrderResponse;
import org.example.awesomepizzabe.services.KitchenManagementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KitchenManagementControllerTest {

    @Mock
    private KitchenManagementService kitchenManagementService;

    @InjectMocks
    private KitchenManagementController controller;

    @Test
    void getPendingOrdersReturnsOkResponseWithOrders() {
        List<OrderResponse> orders = List.of(
                new OrderResponse().orderCode("AP-ABC234"),
                new OrderResponse().orderCode("AP-DEF567")
        );
        when(kitchenManagementService.getPendingOrders()).thenReturn(orders);

        ResponseEntity<List<OrderResponse>> response = controller.getPendingOrders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(orders, response.getBody());
        verify(kitchenManagementService).getPendingOrders();
    }

    @Test
    void getPendingOrdersReturnsOkResponseWhenQueueIsEmpty() {
        List<OrderResponse> orders = List.of();
        when(kitchenManagementService.getPendingOrders()).thenReturn(orders);

        ResponseEntity<List<OrderResponse>> response = controller.getPendingOrders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(orders, response.getBody());
        verify(kitchenManagementService).getPendingOrders();
    }

    @Test
    void takeNextOrderReturnsOkResponseWithTakenOrder() {
        OrderResponse order = new OrderResponse().orderCode("AP-ABC234");
        when(kitchenManagementService.getTakeNextOrder()).thenReturn(order);

        ResponseEntity<OrderResponse> response = controller.takeNextOrder();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(order, response.getBody());
        verify(kitchenManagementService).getTakeNextOrder();
    }

    @Test
    void completeOrderReturnsOkResponseWithCompletedOrder() {
        String orderCode = "AP-ABC234";
        OrderResponse order = new OrderResponse().orderCode(orderCode);
        when(kitchenManagementService.postCompleteOrder(orderCode)).thenReturn(order);

        ResponseEntity<OrderResponse> response = controller.completeOrder(orderCode);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(order, response.getBody());
        verify(kitchenManagementService).postCompleteOrder(orderCode);
    }
}
