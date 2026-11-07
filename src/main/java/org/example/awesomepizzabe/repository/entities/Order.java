package org.example.awesomepizzabe.repository.entities;

import com.matteo.openapi.model.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.awesomepizzabe.utils.OrderCodeGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "order_code",
            nullable = false,
            unique = true,
            length = 20
    )
    private String orderCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 14)
    private OrderStatus status;

    @Column(
            name = "customer_name",
            nullable = false,
            length = 100
    )
    private String customerName;

    @Column(
            name = "customer_phone",
            nullable = false,
            length = 20
    )
    private String customerPhoneNumber;

    @Column(
            name = "delivery_notes",
            length = 225
    )
    private String deliveryNotes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OrderItem> items;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;

        // Genera il codice solo se non è già stato impostato manualmente
        if (this.orderCode == null) {
            this.orderCode = OrderCodeGenerator.generate();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}