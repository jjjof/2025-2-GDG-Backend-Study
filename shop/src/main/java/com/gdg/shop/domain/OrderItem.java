package com.gdg.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "order_item_quantity", nullable = false)
    private int quantity;

    @Column(name = "order_item_price", nullable = false)
    private int orderPrice;

    private OrderItem(int quantity, int orderPrice) {
        this.quantity = quantity;
        this.orderPrice = orderPrice;
    }

    public static OrderItem create(Order order, Product product, int quantity) {
        OrderItem orderItem = new OrderItem(quantity, product.getPrice());

        order.addOrderItem(orderItem);
        product.addOrderItem(orderItem);

        return orderItem;
    }

    void assignOrder(Order order) {
        this.order = order;
    }

    void assignProduct(Product product) {
        this.product = product;
    }

    public int getTotalPrice() {
        return orderPrice * quantity;
    }
}
