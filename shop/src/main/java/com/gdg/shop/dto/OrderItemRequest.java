package com.gdg.shop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class OrderItemRequest {

    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

    @Positive(message = "주문 수량은 1개 이상이어야 합니다.")
    private int quantity;

    public OrderItemRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
