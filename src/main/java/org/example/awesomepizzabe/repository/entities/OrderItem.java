package org.example.awesomepizzabe.repository.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "pizza_name",
            nullable = false,
            length = 100
    )
    private String pizzaName;

    @Column(nullable = false)
    private Long quantity;

    @Column(length = 225)
    private String notes;

    @ManyToOne
    @JoinColumn(
            name = "order_id",
            nullable = false
    )
    private Order order;

}
