package org.example.awesomepizzabe.services;

import com.matteo.openapi.model.OrderResponse;
import com.matteo.openapi.model.OrderStatus;
import org.example.awesomepizzabe.error.exceptions.NoInPreparationOrderException;
import org.example.awesomepizzabe.error.exceptions.NoPendingOrdersException;
import org.example.awesomepizzabe.error.exceptions.RecordNotFoundException;
import org.example.awesomepizzabe.mapper.OrderResponseMapper;
import org.example.awesomepizzabe.repository.OrderRepository;
import org.example.awesomepizzabe.repository.entities.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KitchenManagementServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderResponseMapper orderResponseMapper;

    @InjectMocks
    private KitchenManagementService service;

    @Test
    void getPendingOrdersFindsPendingOrdersInCreationOrderAndMapsThem() {
        List<Order> pendingOrders = List.of(new Order(), new Order());
        List<OrderResponse> expectedResponses = List.of(
                new OrderResponse().orderCode("AP-ABC234"),
                new OrderResponse().orderCode("AP-DEF567")
        );
        when(orderRepository.findByStatusOrderByCreatedAtAsc(OrderStatus.PENDING))
                .thenReturn(pendingOrders);
        when(orderResponseMapper.toOrderResponseList(pendingOrders)).thenReturn(expectedResponses);

        List<OrderResponse> actualResponses = service.getPendingOrders();

        verify(orderRepository).findByStatusOrderByCreatedAtAsc(OrderStatus.PENDING);
        verify(orderResponseMapper).toOrderResponseList(pendingOrders);
        assertSame(expectedResponses, actualResponses);
    }

    @Test
    void getPendingOrdersReturnsEmptyMappedResultWhenQueueIsEmpty() {
        List<Order> pendingOrders = List.of();
        List<OrderResponse> expectedResponses = List.of();
        when(orderRepository.findByStatusOrderByCreatedAtAsc(OrderStatus.PENDING))
                .thenReturn(pendingOrders);
        when(orderResponseMapper.toOrderResponseList(pendingOrders)).thenReturn(expectedResponses);

        List<OrderResponse> actualResponses = service.getPendingOrders();

        verify(orderRepository).findByStatusOrderByCreatedAtAsc(OrderStatus.PENDING);
        verify(orderResponseMapper).toOrderResponseList(pendingOrders);
        assertSame(expectedResponses, actualResponses);
    }

    @Test
    void getTakeNextOrderMovesOldestPendingOrderToPreparationAndMapsIt() {
        Order oldestPendingOrder = new Order();
        oldestPendingOrder.setStatus(OrderStatus.PENDING);
        OrderResponse expectedResponse = new OrderResponse().orderCode("AP-ABC234");
        when(orderRepository.findFirstByStatusOrderByCreatedAtAscIdAsc(OrderStatus.PENDING))
                .thenReturn(Optional.of(oldestPendingOrder));
        when(orderResponseMapper.toOrderResponse(oldestPendingOrder)).thenReturn(expectedResponse);

        OrderResponse actualResponse = service.getTakeNextOrder();

        verify(orderRepository).findFirstByStatusOrderByCreatedAtAscIdAsc(OrderStatus.PENDING);
        verify(orderRepository).save(oldestPendingOrder);
        verify(orderResponseMapper).toOrderResponse(oldestPendingOrder);
        assertEquals(OrderStatus.IN_PREPARATION, oldestPendingOrder.getStatus());
        assertSame(expectedResponse, actualResponse);
    }

    @Test
    void getTakeNextOrderThrowsWhenThereAreNoPendingOrders() {
        when(orderRepository.findFirstByStatusOrderByCreatedAtAscIdAsc(OrderStatus.PENDING))
                .thenReturn(Optional.empty());

        NoPendingOrdersException exception = assertThrows(
                NoPendingOrdersException.class,
                () -> service.getTakeNextOrder()
        );

        assertEquals("No pending orders", exception.getMessage());
        verify(orderRepository).findFirstByStatusOrderByCreatedAtAscIdAsc(OrderStatus.PENDING);
        verifyNoInteractions(orderResponseMapper);
    }

    @Test
    void postCompleteOrderMarksInPreparationOrderCompletedAndMapsIt() {
        String orderCode = "AP-ABC234";
        Order order = new Order();
        order.setStatus(OrderStatus.IN_PREPARATION);
        OrderResponse expectedResponse = new OrderResponse()
                .orderCode(orderCode)
                .status(OrderStatus.COMPLETED);
        when(orderRepository.findByOrderCode(orderCode)).thenReturn(Optional.of(order));
        when(orderResponseMapper.toOrderResponse(order)).thenReturn(expectedResponse);

        OrderResponse actualResponse = service.postCompleteOrder(orderCode);

        verify(orderRepository).findByOrderCode(orderCode);
        verify(orderRepository).save(order);
        verify(orderResponseMapper).toOrderResponse(order);
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
        assertSame(expectedResponse, actualResponse);
    }

    @Test
    void postCompleteOrderThrowsWhenOrderDoesNotExist() {
        String orderCode = "AP-ABC234";
        when(orderRepository.findByOrderCode(orderCode)).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(
                RecordNotFoundException.class,
                () -> service.postCompleteOrder(orderCode)
        );

        assertEquals("Order not found", exception.getMessage());
        verify(orderRepository).findByOrderCode(orderCode);
        verifyNoInteractions(orderResponseMapper);
    }

    @Test
    void postCompleteOrderThrowsWhenOrderIsNotBeingPrepared() {
        String orderCode = "AP-ABC234";
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        when(orderRepository.findByOrderCode(orderCode)).thenReturn(Optional.of(order));

        NoInPreparationOrderException exception = assertThrows(
                NoInPreparationOrderException.class,
                () -> service.postCompleteOrder(orderCode)
        );

        assertEquals("The order is not being prepared", exception.getMessage());
        verify(orderRepository).findByOrderCode(orderCode);
        verifyNoInteractions(orderResponseMapper);
    }
}
