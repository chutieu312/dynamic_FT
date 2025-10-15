package com.dynamic.orders.model;

import com.dynamic.orders.api.OrderStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String item;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    // Default constructor (required by JPA)
    public OrderEntity() {}

    // Constructor for creating new orders
    public OrderEntity(String item, BigDecimal price, OrderStatus status) {
        this.item = item;
        this.price = price;
        this.status = status;
    }

    // Getters and setters
    public Integer getId() { 
        return id; 
    }
    
    public void setId(Integer id) { 
        this.id = id; 
    }

    public String getItem() { 
        return item; 
    }
    
    public void setItem(String item) { 
        this.item = item; 
    }

    public BigDecimal getPrice() { 
        return price; 
    }
    
    public void setPrice(BigDecimal price) { 
        this.price = price; 
    }

    public OrderStatus getStatus() { 
        return status; 
    }
    
    public void setStatus(OrderStatus status) { 
        this.status = status; 
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", item='" + item + '\'' +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}