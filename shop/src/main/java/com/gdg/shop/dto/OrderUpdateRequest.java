package com.gdg.shop.dto;

import com.gdg.shop.domain.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderUpdateRequest {

    @NotNull(message = "변경할 주문 상태는 필수입니다.")
    private OrderStatus status;

    public OrderUpdateRequest(OrderStatus status) {
        this.status = status;
    }
}
