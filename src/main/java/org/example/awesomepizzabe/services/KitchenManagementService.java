package org.example.awesomepizzabe.services;

import com.matteo.openapi.model.OrderResponse;
import com.matteo.openapi.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.example.awesomepizzabe.mapper.OrderResponseMapper;
import org.example.awesomepizzabe.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KitchenManagementService {

    private final OrderRepository orderRepository;
    private final OrderResponseMapper orderResponseMapper;

    public List<OrderResponse> getPendingOrders() {
        return orderResponseMapper.toOrderResponseList(orderRepository.findByStatusOrderByCreatedAtAsc(OrderStatus.PENDING));
    }

    public OrderResponse getTakeNextOrder() {
        // Implement the logic to take the next order from the pending orders
        // This is a placeholder implementation
        return new OrderResponse();
    }

    public OrderResponse getCompleteOrder(String orderCode) {
        // Implement the logic to mark an order as completed
        // This is a placeholder implementation
        return new OrderResponse();
    }

}
