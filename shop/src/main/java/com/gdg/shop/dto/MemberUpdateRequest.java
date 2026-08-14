package com.gdg.shop.dto;

import lombok.Getter;

@Getter
public class MemberUpdateRequest {
    private String password;
    private String phoneNumber;
    private String address;

    public MemberUpdateRequest(String password, String phoneNumber, String address) {
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}

// [질문 정리 1] MemberUpdateRequest는 회원 수정 요청에 필요한 값만 받는 DTO이다.
// 생성 요청과 수정 요청은 필요한 데이터가 다를 수 있으므로 DTO를 분리하는 것이 자연스럽다.
//
// [질문 정리 2] 현재 수정 DTO에는 loginId가 없다.
// 이는 회원의 로그인 아이디는 변경하지 않고 password, phoneNumber, address만 수정한다는 API 의도를 표현한다.
