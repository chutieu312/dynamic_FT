package com.dynamic.orders.service;

import com.dynamic.orders.api.OrderDto;
import com.dynamic.orders.api.CreateOrderRequest;
import com.dynamic.orders.api.UpdateOrderRequest;
import com.dynamic.orders.api.OrderStatus;
import com.dynamic.orders.entity.Order;
import com.dynamic.orders.mapper.OrderMapper;
// import com.dynamic.orders.repository.OrderRepository;  // Future: when adding JPA
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Enhanced OrderService with mapping support
 * Ready for future database integration
 */
@Service
public class OrderServiceWithMapping {
    
    // Current in-memory storage (will be replaced by repository)
    private final List<Order> orders = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    
    // Mapper for DTO <-> Entity conversion
    private final OrderMapper orderMapper;
    
    // Future: JPA Repository
    // private final OrderRepository orderRepository;

    public OrderServiceWithMapping(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
        // this.orderRepository = orderRepository;  // Future injection
    }

    /**
     * Get all orders as DTOs
     */
    public List<OrderDto> list() {
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get order by ID as DTO
     */
    public Optional<OrderDto> get(int id) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .map(orderMapper::toDto);
    }

    /**
     * Create new order from request
     */
    public OrderDto create(String item, BigDecimal price) {
        // Create new entity
        Order order = new Order(item, price, OrderStatus.PENDING);
        order.setId(idGenerator.getAndIncrement());
        
        // Save (currently in-memory, future: database)
        orders.add(order);
        // Future: order = orderRepository.save(order);
        
        // Return as DTO
        return orderMapper.toDto(order);
    }

    /**
     * Update existing order
     */
    public Optional<OrderDto> update(int id, UpdateOrderRequest request) {
        Optional<Order> existingOrder = orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst();
        
        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();
            
            // Update fields from request
            if (request.item() != null) {
                order.setItem(request.item());
            }
            if (request.price() != null) {
                order.setPrice(request.price());
            }
            if (request.status() != null) {
                order.setStatus(request.status());
            }
            
            // Future: orderRepository.save(order);
            
            return Optional.of(orderMapper.toDto(order));
        }
        
        return Optional.empty();
    }

    /**
     * Delete order by ID
     */
    public boolean delete(int id) {
        boolean removed = orders.removeIf(order -> order.getId().equals(id));
        // Future: if (removed) orderRepository.deleteById(id);
        return removed;
    }

    /**
     * Business methods using entities
     */
    public Optional<OrderDto> confirmOrder(int id) {
        return updateOrderStatus(id, OrderStatus.CONFIRMED);
    }

    public Optional<OrderDto> shipOrder(int id) {
        return updateOrderStatus(id, OrderStatus.SHIPPED);
    }

    public Optional<OrderDto> deliverOrder(int id) {
        return updateOrderStatus(id, OrderStatus.DELIVERED);
    }

    public Optional<OrderDto> cancelOrder(int id) {
        return updateOrderStatus(id, OrderStatus.CANCELLED);
    }

    private Optional<OrderDto> updateOrderStatus(int id, OrderStatus status) {
        Optional<Order> orderOpt = orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst();
        
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            // Future: orderRepository.save(order);
            return Optional.of(orderMapper.toDto(order));
        }
        
        return Optional.empty();
    }
}