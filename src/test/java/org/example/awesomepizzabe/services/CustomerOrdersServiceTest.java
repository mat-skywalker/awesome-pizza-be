package org.example.awesomepizzabe.services;

import com.matteo.openapi.model.CreateOrderRequest;
import com.matteo.openapi.model.OrderItemRequest;
import com.matteo.openapi.model.OrderResponse;
import com.matteo.openapi.model.OrderStatus;
import org.example.awesomepizzabe.error.exceptions.RecordNotFoundException;
import org.example.awesomepizzabe.mapper.OrderResponseMapper;
import org.example.awesomepizzabe.repository.OrderRepository;
import org.example.awesomepizzabe.repository.entities.Order;
import org.example.awesomepizzabe.repository.entities.OrderItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerOrdersServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderResponseMapper orderResponseMapper;

    @InjectMocks
    private CustomerOrdersService service;

    @Test
    void createCustomerOrderPersistsTrimmedOrderAndMapsResponse() {
        CreateOrderRequest request = new CreateOrderRequest()
                .customerName("  Ada Lovelace  ")
                .customerPhoneNumber(" 123456 ")
                .deliveryNotes("  Ring the bell  ")
                .addItemsItem(new OrderItemRequest()
                        .pizzaName("  Margherita  ")
                        .quantity(2L)
                        .notes("  No basil  "));
        OrderResponse expectedResponse = new OrderResponse().orderCode("AP-ABC234");
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderResponseMapper.toOrderResponse(any(Order.class))).thenReturn(expectedResponse);

        OrderResponse actualResponse = service.createCustomerOrder(request);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();
        assertEquals("Ada Lovelace", savedOrder.getCustomerName());
        assertEquals("123456", savedOrder.getCustomerPhoneNumber());
        assertEquals("Ring the bell", savedOrder.getDeliveryNotes());
        assertEquals(OrderStatus.PENDING, savedOrder.getStatus());
        assertEquals(1, savedOrder.getItems().size());

        OrderItem savedItem = savedOrder.getItems().get(0);
        assertEquals("Margherita", savedItem.getPizzaName());
        assertEquals(2L, savedItem.getQuantity());
        assertEquals("No basil", savedItem.getNotes());
        assertSame(savedOrder, savedItem.getOrder());
        verify(orderResponseMapper).toOrderResponse(savedOrder);
        assertSame(expectedResponse, actualResponse);
    }

    @Test
    void getCustomerOrderFindsAndMapsOrder() {
        String orderCode = "AP-ABC234";
        Order order = new Order();
        OrderResponse expectedResponse = new OrderResponse().orderCode(orderCode);
        when(orderRepository.findByOrderCode(orderCode)).thenReturn(Optional.of(order));
        when(orderResponseMapper.toOrderResponse(order)).thenReturn(expectedResponse);

        OrderResponse actualResponse = service.getCustomerOrder(orderCode);

        verify(orderRepository).findByOrderCode(orderCode);
        verify(orderResponseMapper).toOrderResponse(order);
        assertSame(expectedResponse, actualResponse);
    }

    @Test
    void getCustomerOrderThrowsWhenOrderDoesNotExist() {
        String orderCode = "AP-ABC234";
        when(orderRepository.findByOrderCode(orderCode)).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(
                RecordNotFoundException.class,
                () -> service.getCustomerOrder(orderCode)
        );

        assertEquals("Order not found", exception.getMessage());
        verify(orderRepository).findByOrderCode(orderCode);
        verifyNoInteractions(orderResponseMapper);
    }
}
