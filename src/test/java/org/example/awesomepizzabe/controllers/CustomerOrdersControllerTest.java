package org.example.awesomepizzabe.controllers;

import com.matteo.openapi.model.CreateOrderRequest;
import com.matteo.openapi.model.OrderItemRequest;
import com.matteo.openapi.model.OrderResponse;
import org.example.awesomepizzabe.services.CustomerOrdersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerOrdersControllerTest {

    @Mock
    private CustomerOrdersService customerOrdersService;

    @InjectMocks
    private CustomerOrdersController controller;

    @Test
    void createOrderReturnsCreatedResponse() {
        CreateOrderRequest request = new CreateOrderRequest().customerName("Ada")
                .customerPhoneNumber("123456")
                .addItemsItem(new OrderItemRequest().pizzaName("Margherita").quantity(1L));
        OrderResponse orderResponse = new OrderResponse().orderCode("AP-ABC234");
        when(customerOrdersService.createCustomerOrder(request)).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> response = controller.createOrder(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(orderResponse, response.getBody());
        verify(customerOrdersService).createCustomerOrder(request);
    }

    @Test
    void getOrderByCodeReturnsOkResponse() {
        String orderCode = "AP-ABC234";
        OrderResponse orderResponse = new OrderResponse().orderCode(orderCode);
        when(customerOrdersService.getCustomerOrder(orderCode)).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> response = controller.getOrderByCode(orderCode);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(orderResponse, response.getBody());
        verify(customerOrdersService).getCustomerOrder(orderCode);
    }
}
