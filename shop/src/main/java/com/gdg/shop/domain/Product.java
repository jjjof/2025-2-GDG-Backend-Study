package com.gdg.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gdg.shop.common.exception.BadRequestException;
import com.gdg.shop.common.message.ErrorMessage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name", length = 100, nullable = false)
    private String name;

    @Column(name = "product_price", nullable = false)
    private int price;

    @Column(name = "product_stock_quantity", nullable = false)
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status", length = 20, nullable = false)
    private ProductStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems = new ArrayList<>();

    public Product(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        updateStatusFromStock();
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.assignProduct(this);
    }

    public void decreaseStock(int quantity) {
        if (this.stockQuantity < quantity) {
            throw new BadRequestException(ErrorMessage.Product_Stock_Not_Enough);
        }

        this.stockQuantity -= quantity;
        updateStatusFromStock();
    }

    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
        updateStatusFromStock();
    }

    private void updateStatusFromStock() {
        this.status = (this.stockQuantity == 0)
                ? ProductStatus.SOLD_OUT
                : ProductStatus.AVAILABLE;
    }
}
