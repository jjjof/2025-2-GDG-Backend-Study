package com.gdg.shop.common.message;

public class ErrorMessage {

    //Member 관련 에러 메시지
    public static final String Member_Not_Found = "회원을 찾을 수 없습니다.";
    public static final String Member_Already_Exists = "이미 존재하는 회원입니다.";

    //DTO
    public static final String LOGIN_ID_NOT_NULL = "로그인 아이디는 필수입니다.";
    public static final String LOGIN_ID_SIZE = "로그인 아이디는 4자 이상 20자 이하입니다.";
    public static final String PASSWORD_NOT_NULL = "비밀번호는 필수입니다.";
    public static final String PASSWORD_SIZE = "비밀번호는 8자 이상 20자 이하입니다.";
    public static final String PHONE_NUMBER_NOT_NULL = "전화번호는 필수입니다.";
    public static final String PHONE_NUMBER_PATTERN = "전화번호 형식은 010-xxxx-xxxx 입니다.";
    public static final String ADDRESS_NOT_NULL = "주소는 필수입니다.";
    public static final String ADDRESS_SIZE = "주소는 1자 이상 255자 이하입니다.";
}
