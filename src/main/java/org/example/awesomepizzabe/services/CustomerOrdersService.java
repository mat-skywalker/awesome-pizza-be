package org.example.awesomepizzabe.services;

import com.matteo.openapi.model.CreateOrderRequest;
import com.matteo.openapi.model.OrderResponse;
import com.matteo.openapi.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.awesomepizzabe.mapper.OrderResponseMapper;
import org.example.awesomepizzabe.repository.OrderRepository;
import org.example.awesomepizzabe.repository.entities.Order;
import org.example.awesomepizzabe.repository.entities.OrderItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerOrdersService {

    private final OrderRepository orderRepository;
    private final OrderResponseMapper orderResponseMapper;

    public OrderResponse createCustomerOrder(CreateOrderRequest createOrderRequest) {
        Order order = new Order();

        order.setCustomerName(StringUtils.trim(createOrderRequest.getCustomerName()));
        order.setCustomerPhoneNumber(StringUtils.trim(createOrderRequest.getCustomerPhoneNumber()));
        order.setDeliveryNotes(StringUtils.trim(createOrderRequest.getDeliveryNotes()));
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();

        createOrderRequest.getItems().forEach(itemRequest -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setPizzaName(StringUtils.trim(itemRequest.getPizzaName()));
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        });
        order.setItems(orderItems);

        return orderResponseMapper.toOrderResponse(orderRepository.save(order));
    }

    public OrderResponse getCustomerOrder(String orderCode) {
        // Implement the logic to retrieve a customer order by its code
        // This is a placeholder implementation
        return new OrderResponse();
    }

}
