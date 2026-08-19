package com.gdg.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProductCreateRequest {

    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 100, message = "상품명은 100자 이하입니다.")
    private String name;

    @Positive(message = "상품 가격은 0원보다 커야 합니다.")
    private int price;

    @PositiveOrZero(message = "상품 재고는 0개 이상이어야 합니다.")
    private int stockQuantity;

    public ProductCreateRequest(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}
