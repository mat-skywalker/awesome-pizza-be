package org.example.awesomepizzabe.services;

import com.matteo.openapi.model.OrderResponse;
import com.matteo.openapi.model.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.awesomepizzabe.error.exceptions.NoPendingOrdersException;
import org.example.awesomepizzabe.mapper.OrderResponseMapper;
import org.example.awesomepizzabe.repository.OrderRepository;
import org.example.awesomepizzabe.repository.entities.Order;
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

    @Transactional
    public OrderResponse getTakeNextOrder() {

        Order order = orderRepository
                .findFirstByStatusOrderByCreatedAtAscIdAsc(OrderStatus.PENDING)
                .orElseThrow(() -> new NoPendingOrdersException("No pending orders"));

        order.setStatus(OrderStatus.IN_PREPARATION);
        orderRepository.save(order);

        return orderResponseMapper.toOrderResponse(order);
    }

    public OrderResponse getCompleteOrder(String orderCode) {
        // Implement the logic to mark an order as completed
        // This is a placeholder implementation
        return new OrderResponse();
    }

}
