package com.gdg.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(name = "order_cash_amount")
    private int cashAmount;

    @Column(name = "order_status", length = 25)
    private String status;

    public Order(Member member, LocalDateTime orderDate, int totalPrice, int cashAmount, String status) {
        this.member = member;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.cashAmount = cashAmount;
        this.status = status;
    }
}