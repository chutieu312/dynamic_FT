package com.dynamic.orders.mapper;

import com.dynamic.orders.api.OrderDto;
import com.dynamic.orders.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    /**
     * Convert Order entity to OrderDto for API responses
     */
    public OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }
        
        return new OrderDto(
            order.getId(),
            order.getItem(),
            order.getPrice(),
            order.getStatus()
        );
    }

    /**
     * Convert OrderDto to Order entity for database operations
     */
    public Order toEntity(OrderDto dto) {
        if (dto == null) {
            return null;
        }
        
        Order order = new Order();
        order.setId(dto.id());
        order.setItem(dto.item());
        order.setPrice(dto.price());
        order.setStatus(dto.status());
        
        return order;
    }

    /**
     * Update existing Order entity with OrderDto data
     */
    public void updateEntity(OrderDto dto, Order existingOrder) {
        if (dto == null || existingOrder == null) {
            return;
        }
        
        if (dto.item() != null) {
            existingOrder.setItem(dto.item());
        }
        if (dto.price() != null) {
            existingOrder.setPrice(dto.price());
        }
        if (dto.status() != null) {
            existingOrder.setStatus(dto.status());
        }
    }
}