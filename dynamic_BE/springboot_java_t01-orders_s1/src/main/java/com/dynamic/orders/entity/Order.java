package com.dynamic.orders.entity;

import com.dynamic.orders.api.OrderStatus;
// Future JPA imports (uncomment when adding JPA dependency):
// import jakarta.persistence.*;
// import org.hibernate.annotations.CreationTimestamp;
// import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Order Entity for database persistence
 * This will be used when you add JPA/Database support
 * 
 * To enable this:
 * 1. Add spring-boot-starter-data-jpa to build.gradle
 * 2. Add database dependency (H2, PostgreSQL, etc.)
 * 3. Configure datasource in application.properties
 */
// @Entity  // Uncomment when adding JPA
// @Table(name = "orders")  // Uncomment when adding JPA
public class Order {
    
    // @Id  // Uncomment when adding JPA
    // @GeneratedValue(strategy = GenerationType.IDENTITY)  // Uncomment when adding JPA
    private Integer id;
    
    // @Column(nullable = false)  // Uncomment when adding JPA
    private String item;
    
    // @Column(nullable = false, precision = 10, scale = 2)  // Uncomment when adding JPA
    private BigDecimal price;
    
    // @Enumerated(EnumType.STRING)  // Uncomment when adding JPA
    private OrderStatus status;
    
    // @CreationTimestamp  // Uncomment when adding JPA
    // @Column(name = "created_at", nullable = false, updatable = false)  // Uncomment when adding JPA
    private LocalDateTime createdAt;
    
    // @UpdateTimestamp  // Uncomment when adding JPA
    // @Column(name = "updated_at")  // Uncomment when adding JPA
    private LocalDateTime updatedAt;
    
    // @Column(name = "customer_id")  // Uncomment when adding JPA
    private Long customerId;
    
    // @Column(name = "internal_notes", length = 500)  // Uncomment when adding JPA
    private String internalNotes;

    // Default constructor (required by JPA)
    public Order() {}

    // Constructor for business logic
    public Order(String item, BigDecimal price, OrderStatus status) {
        this.item = item;
        this.price = price;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getInternalNotes() {
        return internalNotes;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
    }

    // Business methods
    public void markAsConfirmed() {
        this.status = OrderStatus.CONFIRMED;
    }

    public void markAsShipped() {
        this.status = OrderStatus.SHIPPED;
    }

    public void markAsDelivered() {
        this.status = OrderStatus.DELIVERED;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

    public boolean isActive() {
        return status != OrderStatus.CANCELLED && status != OrderStatus.DELIVERED;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", item='" + item + '\'' +
                ", price=" + price +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}