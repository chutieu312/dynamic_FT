package com.dynamic.orders.mapper;

import com.dynamic.orders.api.OrderDto;
import com.dynamic.orders.api.CreateOrderRequest;
import com.dynamic.orders.api.UpdateOrderRequest;
import com.dynamic.orders.api.OrderStatus;
import com.dynamic.orders.model.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    /**
     * Convert OrderEntity to OrderDto for API responses
     */
    public OrderDto toDto(OrderEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new OrderDto(
            entity.getId(),
            entity.getItem(),
            entity.getPrice(),
            entity.getStatus()
        );
    }

    /**
     * Convert OrderDto to OrderEntity for database operations
     */
    public OrderEntity toEntity(OrderDto dto) {
        if (dto == null) {
            return null;
        }
        
        OrderEntity entity = new OrderEntity();
        entity.setId(dto.id());
        entity.setItem(dto.item());
        entity.setPrice(dto.price());
        entity.setStatus(dto.status());
        
        return entity;
    }

    /**
     * Create new OrderEntity from CreateOrderRequest
     */
    public OrderEntity toEntity(CreateOrderRequest request) {
        if (request == null) {
            return null;
        }
        
        OrderEntity entity = new OrderEntity();
        entity.setItem(request.item());
        entity.setPrice(request.price());
        entity.setStatus(OrderStatus.PENDING); // Default status for new orders
        
        return entity;
    }

    /**
     * Update existing OrderEntity with UpdateOrderRequest data
     */
    public void updateEntity(UpdateOrderRequest request, OrderEntity existingEntity) {
        if (request == null || existingEntity == null) {
            return;
        }
        
        if (request.item() != null) {
            existingEntity.setItem(request.item());
        }
        if (request.price() != null) {
            existingEntity.setPrice(request.price());
        }
        if (request.status() != null) {
            existingEntity.setStatus(request.status());
        }
    }

    /**
     * Update existing OrderEntity with OrderDto data
     */
    public void updateEntity(OrderDto dto, OrderEntity existingEntity) {
        if (dto == null || existingEntity == null) {
            return;
        }
        
        if (dto.item() != null) {
            existingEntity.setItem(dto.item());
        }
        if (dto.price() != null) {
            existingEntity.setPrice(dto.price());
        }
        if (dto.status() != null) {
            existingEntity.setStatus(dto.status());
        }
    }
}