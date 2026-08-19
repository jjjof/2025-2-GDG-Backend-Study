package com.gdg.shop.controller;

import com.gdg.shop.domain.Order;
import com.gdg.shop.dto.OrderCreateRequest;
import com.gdg.shop.service.OrderServ;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@ResponseBody
@Tag(name = "주문 관리", description = "주문 생성, 조회 및 취소 API")
public class OrderCon {

    private final OrderServ orderServ;

    @PostMapping
    @Operation(summary = "주문 생성", description = "회원과 주문 상품 목록을 이용하여 새로운 주문을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "주문 생성 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패 또는 상품 재고 부족"),
            @ApiResponse(responseCode = "404", description = "회원 또는 상품을 찾을 수 없음")
    })
    public ResponseEntity<Void> createOrder(@RequestBody @Valid OrderCreateRequest request) {
        Long orderId = orderServ.createOrder(request);

        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping
    @Operation(summary = "전체 주문 조회", description = "등록된 모든 주문과 주문 상품을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "전체 주문 조회 성공")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderServ.findAllOrders();

        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{orderId}/cancel")
    @Operation(summary = "주문 취소", description = "주문을 취소하고 주문 상품의 재고를 복구합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 취소 성공"),
            @ApiResponse(responseCode = "400", description = "현재 상태에서는 주문을 취소할 수 없음"),
            @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음")
    })
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderServ.cancelOrder(orderId);

        return ResponseEntity.ok().build();
    }

}
