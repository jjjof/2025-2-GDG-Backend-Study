package com.gdg.shop.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import static com.gdg.shop.common.message.ErrorMessage.ADDRESS_SIZE;
import static com.gdg.shop.common.message.ErrorMessage.PASSWORD_SIZE;
import static com.gdg.shop.common.message.ErrorMessage.PHONE_NUMBER_PATTERN;

@Getter
public class MemberUpdateRequest {

    @Size(min=8,max=20,message=PASSWORD_SIZE)
    private String password;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = PHONE_NUMBER_PATTERN)
    private String phoneNumber;

    @Size(min=1,max=55,message=ADDRESS_SIZE)
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
