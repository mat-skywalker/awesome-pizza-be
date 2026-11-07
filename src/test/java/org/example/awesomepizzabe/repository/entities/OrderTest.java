package org.example.awesomepizzabe.repository.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTest {

    @Test
    void onCreateSetsTimestampsAndGeneratesCodeWhenMissing() {
        Order order = new Order();
        LocalDateTime beforeCreate = LocalDateTime.now();

        order.onCreate();

        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getUpdatedAt());
        assertTrue(!order.getCreatedAt().isBefore(beforeCreate));
        assertEquals(order.getCreatedAt(), order.getUpdatedAt());
        assertTrue(order.getOrderCode().matches("AP-[23456789ABCDEFGHJKLMNPQRSTUVWXYZ]{6}"));
    }

    @Test
    void onCreatePreservesExistingCode() {
        Order order = new Order();
        order.setOrderCode("AP-ABC234");
        order.onCreate();

        assertEquals("AP-ABC234", order.getOrderCode());
    }
}
