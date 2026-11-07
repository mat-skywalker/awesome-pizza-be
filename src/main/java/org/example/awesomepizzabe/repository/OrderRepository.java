package org.example.awesomepizzabe.repository;

import com.matteo.openapi.model.OrderStatus;
import org.example.awesomepizzabe.repository.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderCode(String orderCode);

    List<Order> findByStatusOrderByCreatedAtAsc(OrderStatus status);

    Optional<Order> findFirstByStatusOrderByCreatedAtAscIdAsc(OrderStatus status);

}
