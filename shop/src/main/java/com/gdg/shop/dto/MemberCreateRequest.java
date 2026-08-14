package com.gdg.shop.dto;

import lombok.Getter;

@Getter
public class MemberCreateRequest {
    private String password;
    private String loginId;
    private String phoneNumber;
    private String address;


    public MemberCreateRequest(String loginId, String password, String address, String phoneNumber) {
        this.password = password;
        this.loginId = loginId;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}

// [질문 정리 1] DTO는 API 요청/응답에서 사용할 데이터 모양을 따로 정의하는 객체이다.
// Entity를 직접 요청/응답에 쓰지 않고 DTO를 사용하면 DB 구조와 API 구조를 분리할 수 있다.
//
// [질문 정리 2] MemberCreateRequest는 회원 생성 요청에 필요한 값만 받는 요청 DTO이다.
// 회원 생성 시 클라이언트가 id나 point를 직접 정하지 않도록 Entity와 요청 데이터를 분리한다.
//
// [질문 정리 3] DTO 패키지는 controller/service/repository와 같은 계층형 패키지 옆에 둘 수 있다.
// 프로젝트가 커지면 dto/member, dto/product처럼 도메인별로 나누는 방식도 많이 사용한다.
