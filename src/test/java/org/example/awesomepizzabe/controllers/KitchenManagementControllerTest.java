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
}
