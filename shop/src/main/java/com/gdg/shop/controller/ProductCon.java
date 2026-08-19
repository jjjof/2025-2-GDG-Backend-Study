package com.gdg.shop.controller;

import com.gdg.shop.dto.ProductCreateRequest;
import com.gdg.shop.service.ProductServ;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@Tag(name = "상품 관리", description = "상품 관리 API")
public class ProductCon {

    private final ProductServ productServ;

    @PostMapping
    @Operation(summary = "상품 등록", description = "상품명, 가격, 초기 재고를 입력하여 상품을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "상품 등록 성공"),
            @ApiResponse(responseCode = "400", description = "상품 입력값 유효성 검사 실패")
    })
    public ResponseEntity<Void> createProduct(
            @RequestBody @Valid ProductCreateRequest request
    ) {
        Long productId = productServ.createProduct(request);

        return ResponseEntity.created(URI.create("/products/" + productId)).build();
    }
}
