package com.dynamic.orders.api;

import java.math.BigDecimal;

public record OrderDto(
        Integer id,
        String item,
        BigDecimal price,
        OrderStatus status
) {}

