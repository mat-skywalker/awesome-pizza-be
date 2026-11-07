package org.example.awesomepizzabe.mapper;

import com.matteo.openapi.model.OrderStatus;
import org.example.awesomepizzabe.repository.entities.Order;
import org.example.awesomepizzabe.repository.entities.OrderItem;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderResponseMapperTest {

    private final OrderResponseMapper mapper = Mappers.getMapper(OrderResponseMapper.class);

    @Test
    void mapsOrderAndItsItemsToResponse() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 11, 7, 18, 30);
        LocalDateTime updatedAt = createdAt.plusMinutes(5);
        Order order = new Order();
        order.setOrderCode("AP-ABC234");
        order.setStatus(OrderStatus.IN_PREPARATION);
        order.setCustomerName("Ada Lovelace");
        order.setDeliveryNotes("Ring the bell");
        order.setCreatedAt(createdAt);
        order.setUpdatedAt(updatedAt);

        OrderItem item = new OrderItem();
        item.setId(42L);
        item.setPizzaName("Margherita");
        item.setQuantity(2L);
        item.setNotes("No basil");
        item.setOrder(order);
        order.setItems(List.of(item));

        var response = mapper.toOrderResponse(order);

        assertAll(
                () -> assertEquals("AP-ABC234", response.getOrderCode()),
                () -> assertEquals(OrderStatus.IN_PREPARATION, response.getStatus()),
                () -> assertEquals("Ada Lovelace", response.getCustomerName()),
                () -> assertEquals("Ring the bell", response.getDeliveryNotes()),
                () -> assertEquals(createdAt, response.getCreatedAt()),
                () -> assertEquals(updatedAt, response.getUpdatedAt()),
                () -> assertEquals(1, response.getItems().size()),
                () -> assertEquals(42L, response.getItems().get(0).getId()),
                () -> assertEquals("Margherita", response.getItems().get(0).getPizzaName()),
                () -> assertEquals(2L, response.getItems().get(0).getQuantity()),
                () -> assertEquals("No basil", response.getItems().get(0).getNotes())
        );
    }

    @Test
    void mapsOrderListsAndPreservesNullInputBehavior() {
        Order firstOrder = new Order();
        firstOrder.setOrderCode("AP-ABC234");
        Order secondOrder = new Order();
        secondOrder.setOrderCode("AP-DEF567");

        var responses = mapper.toOrderResponseList(List.of(firstOrder, secondOrder));

        assertEquals(List.of("AP-ABC234", "AP-DEF567"),
                responses.stream().map(com.matteo.openapi.model.OrderResponse::getOrderCode).toList());
        assertNull(mapper.toOrderResponse(null));
        assertNull(mapper.toOrderResponseList(null));
    }
}
