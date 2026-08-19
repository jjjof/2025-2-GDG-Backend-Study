package com.gdg.shop.common;

import com.gdg.shop.common.dto.ErrorResponse;
import com.gdg.shop.common.exception.BadRequestException;
import com.gdg.shop.common.exception.NotFoundException;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    //400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ErrorResponse errorResponse = new ErrorResponse(message);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    //400
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);

    }

    //404
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        //return ResponseEntity.notFound().build();
    }

    //500
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknownException(Exception e){
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

}

// [질문 정리 1] @ControllerAdvice는 모든 Controller에서 발생한 예외를 중앙에서 처리하도록 등록하는 어노테이션이다.
// AOP처럼 공통 관심사를 분리하는 느낌은 있지만, 실제 동작은 Spring MVC의 예외 처리 흐름에서 HandlerExceptionResolver가 이 클래스를 참고하는 방식이다.
//
// [질문 정리 2] @ExceptionHandler(A.class)는 A 타입 예외가 발생했을 때 어떤 메서드가 응답을 만들지 연결한다.
// 예를 들어 @ExceptionHandler(NotFoundException.class)는 NotFoundException 발생 시 handleNotFoundException 메서드를 실행한다.
//
// [질문 정리 3] @ExceptionHandler(Exception.class)는 Exception의 하위 타입 예외를 처리한다.
// 더 구체적인 예외 핸들러가 없으면 마지막에 잡히는 기본 예외 처리기 역할을 한다.
//
// [질문 정리 4] Exception e는 실제 발생한 예외 객체이다.
// e.getMessage()로 예외 메시지를 꺼낼 수 있고, e.getClass(), e.getStackTrace(), e.getCause() 등으로 예외 타입, 발생 위치, 원인 예외를 확인할 수 있다.
//
// [질문 정리 5] ResponseEntity.internalServerError()는 500 Internal Server Error 상태 코드를 가진 응답을 만들기 시작하는 메서드이다.
// 뒤에 .body(errorResponse)를 붙이면 HTTP 응답 body에 ErrorResponse 객체가 JSON 형태로 들어간다.
//
// [질문 정리 6] Postman Body 탭에 보이는 JSON은 HTTP 응답 전체가 아니라 응답 body이다.
// 실제 HTTP 응답은 상태 코드, 헤더, body로 나뉘며, body 안에 status/message/path 같은 JSON 필드가 들어갈 수 있다.
