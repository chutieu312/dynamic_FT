package com.dynamic.orders.api;

import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/** All fields optional; only provided ones will be updated. */
public record UpdateOrderRequest(
        String item,
        @PositiveOrZero BigDecimal price,
        OrderStatus status
) {}

