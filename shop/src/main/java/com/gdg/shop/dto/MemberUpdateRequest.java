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
