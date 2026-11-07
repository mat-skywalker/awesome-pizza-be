package org.example.awesomepizzabe.repository;

import com.matteo.openapi.model.OrderStatus;
import jakarta.persistence.LockModeType;
import org.example.awesomepizzabe.repository.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderCode(String orderCode);

    List<Order> findByStatusOrderByCreatedAtAsc(OrderStatus status);

    //It locks the database row until a commit o rollback, to avoid conflicts in case of multiple calls
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Order> findFirstByStatusOrderByCreatedAtAscIdAsc(OrderStatus status);

}
