package com.gdg.shop.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String message;
}

// [질문 정리] ErrorResponse는 에러가 났을 때 HTTP 응답 body에 담아 보낼 DTO이다.
// message 필드는 Postman Body 탭에 JSON의 "message" 값으로 표시된다.
