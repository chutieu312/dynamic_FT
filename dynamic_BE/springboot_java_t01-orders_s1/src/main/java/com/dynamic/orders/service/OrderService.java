package com.dynamic.orders.service;

import com.dynamic.orders.api.OrderDto;
import com.dynamic.orders.api.CreateOrderRequest;
import com.dynamic.orders.api.UpdateOrderRequest;
import com.dynamic.orders.api.OrderStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {
    private final List<OrderDto> orders = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public List<OrderDto> list() {
        return new ArrayList<>(orders);
    }

    public Optional<OrderDto> get(int id) {
        return orders.stream()
                .filter(order -> order.id().equals(id))
                .findFirst();
    }

    public OrderDto create(String item, BigDecimal price) {
        OrderDto newOrder = new OrderDto(
                idGenerator.getAndIncrement(),
                item,
                price,
                OrderStatus.PENDING
        );
        orders.add(newOrder);
        return newOrder;
    }

    public Optional<OrderDto> update(int id, UpdateOrderRequest request) {
        for (int i = 0; i < orders.size(); i++) {
            OrderDto order = orders.get(i);
            if (order.id().equals(id)) {
                OrderDto updatedOrder = new OrderDto(
                        order.id(),
                        request.item() != null ? request.item() : order.item(),
                        request.price() != null ? request.price() : order.price(),
                        request.status() != null ? request.status() : order.status()
                );
                orders.set(i, updatedOrder);
                return Optional.of(updatedOrder);
            }
        }
        return Optional.empty();
    }

    public boolean delete(int id) {
        return orders.removeIf(order -> order.id().equals(id));
    }
}