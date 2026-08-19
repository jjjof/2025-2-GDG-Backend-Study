package com.gdg.shop.controller;

import com.gdg.shop.dto.OrderUpdateRequest;
import com.gdg.shop.service.OrderServ;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/orders")
@Tag(name = "관리자 주문 관리", description = "관리자용 주문 상태 변경 API")
public class AdminOrderController {

    private final OrderServ orderServ;

    @PatchMapping("/{orderId}/status")
    @Operation(summary = "주문 상태 변경", description = "관리자가 주문 상태를 허용된 다음 상태로 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 상태 변경 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패 또는 허용되지 않은 상태 전이"),
            @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음")
    })
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderUpdateRequest request
    ) {
        orderServ.updateOrderStatus(orderId, request);

        return ResponseEntity.ok().build();
    }
}
