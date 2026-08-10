package com.gdg.shop.dto;

import lombok.Getter;

@Getter
public class MemberCreateRequest {
    private String password;
    private String loginId;
    private String phoneNumber;
    private String address;


    public MemberCreateRequest(String password, String loginId, String phoneNumber, String address) {
        this.password = password;
        this.loginId = loginId;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
