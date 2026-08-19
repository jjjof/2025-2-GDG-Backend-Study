package com.gdg.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders")
public class Order{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "order_total_price")
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", length = 25)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order(Member member, LocalDateTime orderDate) {
        this.member = member;
        this.orderDate = orderDate;
        this.totalPrice = 0;
        this.status = OrderStatus.ORDERED;
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.assignOrder(this);
    }

    public void calculateTotalPrice() {
        this.totalPrice = orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }
}
