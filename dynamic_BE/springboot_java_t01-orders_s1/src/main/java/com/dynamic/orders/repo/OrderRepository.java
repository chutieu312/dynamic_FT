package com.dynamic.orders.repo;

import com.dynamic.orders.api.OrderStatus;
import com.dynamic.orders.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    // JpaRepository provides all basic CRUD operations:
    // - save(OrderEntity entity)         -> INSERT/UPDATE
    // - findById(Integer id)             -> SELECT by ID  
    // - findAll()                        -> SELECT all
    // - deleteById(Integer id)           -> DELETE by ID
    // - existsById(Integer id)           -> CHECK existence
    // - count()                          -> COUNT records
    
    // CUSTOM QUERY METHODS (Auto-implemented by Spring Data JPA)
    
    // Find by status
    List<OrderEntity> findByStatus(OrderStatus status);
    // → SELECT * FROM orders WHERE status = ?
    
    // Find by item name (partial match)
    List<OrderEntity> findByItemContaining(String item);
    // → SELECT * FROM orders WHERE item LIKE %?%
    
    // Find by price comparison
    List<OrderEntity> findByPriceGreaterThan(BigDecimal price);
    // → SELECT * FROM orders WHERE price > ?
    
    List<OrderEntity> findByPriceLessThanEqual(BigDecimal price);
    // → SELECT * FROM orders WHERE price <= ?
    
    // Find by multiple conditions
    List<OrderEntity> findByStatusAndPriceGreaterThan(OrderStatus status, BigDecimal price);
    // → SELECT * FROM orders WHERE status = ? AND price > ?
    
    // Count by status
    long countByStatus(OrderStatus status);
    // → SELECT COUNT(*) FROM orders WHERE status = ?
    
    // Find most expensive orders first
    List<OrderEntity> findByStatusOrderByPriceDesc(OrderStatus status);
    // → SELECT * FROM orders WHERE status = ? ORDER BY price DESC
}

// 🎨 Method Naming Keywords:
// Keyword	SQL Equivalent	Example
// findBy	SELECT * WHERE	findByStatus(status)
// countBy	SELECT COUNT(*) WHERE	countByStatus(status)
// deleteBy	DELETE WHERE	deleteByStatus(status)
// existsBy	SELECT COUNT(*) > 0 WHERE	existsByItem(item)
// GreaterThan	> ?	findByPriceGreaterThan(price)
// LessThan	< ?	findByPriceLessThan(price)
// GreaterThanEqual	>= ?	findByPriceGreaterThanEqual(price)
// LessThanEqual	<= ?	findByPriceLessThanEqual(price)
// Like	LIKE ?	findByItemLike("%coffee%")
// Containing	LIKE %?%	findByItemContaining("coffee")
// StartingWith	LIKE ?%	findByItemStartingWith("cof")
// EndingWith	LIKE %?	findByItemEndingWith("fee")
// And	AND	findByStatusAndPrice(status, price)
// Or	OR	findByStatusOrPrice(status, price)
// OrderBy	ORDER BY	findByStatusOrderByPriceDesc(status)
// Top / First	LIMIT	findTop5ByStatus(status)