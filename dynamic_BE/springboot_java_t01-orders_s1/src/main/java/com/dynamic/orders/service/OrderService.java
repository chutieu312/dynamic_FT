package com.dynamic.orders.service;

import com.dynamic.orders.api.*;
import com.dynamic.orders.model.OrderEntity;
import com.dynamic.orders.repo.OrderRepository;
import com.dynamic.orders.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository repo;
    private final OrderMapper mapper;

    public OrderService(OrderRepository repo, OrderMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<OrderDto> list() {
        return repo.findAll()
                  .stream()
                  .map(mapper::toDto)
                  .toList();
    }

    @Transactional(readOnly = true)
    public Optional<OrderDto> get(int id) {
        return repo.findById(id)
                  .map(mapper::toDto);
    }

    @Transactional
    public OrderDto create(CreateOrderRequest req) {
        OrderEntity entity = mapper.toEntity(req);
        OrderEntity savedEntity = repo.save(entity);
        return mapper.toDto(savedEntity);
    }

    @Transactional
    public Optional<OrderDto> update(int id, UpdateOrderRequest req) {
        return repo.findById(id).map(entity -> {
            mapper.updateEntity(req, entity);
            OrderEntity updatedEntity = repo.save(entity);
            return mapper.toDto(updatedEntity);
        });
    }

    @Transactional
    public boolean delete(int id) {
        if (!repo.existsById(id)) {
            return false;
        }
        repo.deleteById(id);
        return true;
    }

    // Additional business methods using the mapper

    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByStatus(OrderStatus status) {
        return repo.findByStatus(status)
                  .stream()
                  .map(mapper::toDto)
                  .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderDto> searchByItem(String item) {
        return repo.findByItemContaining(item)
                  .stream()
                  .map(mapper::toDto)
                  .toList();
    }

    @Transactional(readOnly = true)
    public long getOrderCount() {
        return repo.count();
    }

    @Transactional(readOnly = true)
    public long getOrderCountByStatus(OrderStatus status) {
        return repo.countByStatus(status);
    }
}