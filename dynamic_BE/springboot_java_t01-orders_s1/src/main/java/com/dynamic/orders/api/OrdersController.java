package com.dynamic.orders.api;

import com.dynamic.orders.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "http://localhost:4011")
public class OrdersController {

    private final OrderService svc;

    public OrdersController(OrderService svc) {
        this.svc = svc;
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @GetMapping
    public List<OrderDto> list() {
        return svc.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> get(@PathVariable int id) {
        return svc.get(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody @Valid CreateOrderRequest req) {
        var created = svc.create(req);
        return ResponseEntity.created(URI.create("/api/v1/orders/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> update(@PathVariable int id, @RequestBody @Valid UpdateOrderRequest req) {
        return svc.update(id, req).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return svc.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Additional business endpoints

    @GetMapping("/status/{status}")
    public List<OrderDto> getByStatus(@PathVariable OrderStatus status) {
        return svc.getOrdersByStatus(status);
    }

    @GetMapping("/search")
    public List<OrderDto> searchByItem(@RequestParam String item) {
        return svc.searchByItem(item);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getOrderCounts() {
        long totalCount = svc.getOrderCount();
        long pendingCount = svc.getOrderCountByStatus(OrderStatus.PENDING);
        long confirmedCount = svc.getOrderCountByStatus(OrderStatus.CONFIRMED);
        long shippedCount = svc.getOrderCountByStatus(OrderStatus.SHIPPED);
        long deliveredCount = svc.getOrderCountByStatus(OrderStatus.DELIVERED);
        long cancelledCount = svc.getOrderCountByStatus(OrderStatus.CANCELLED);

        var counts = Map.of(
            "total", totalCount,
            "pending", pendingCount,
            "confirmed", confirmedCount,
            "shipped", shippedCount,
            "delivered", deliveredCount,
            "cancelled", cancelledCount
        );
        
        return ResponseEntity.ok(counts);
    }
}
