package com.gdg.shop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateRequest {

    @NotNull(message = "회원 ID는 필수입니다.")
    private Long memberId;

    @Valid
    @NotEmpty(message = "주문 상품은 하나 이상이어야 합니다.")
    private List<OrderItemRequest> items;

    public OrderCreateRequest(Long memberId, List<OrderItemRequest> items) {
        this.memberId = memberId;
        this.items = items;
    }
}
