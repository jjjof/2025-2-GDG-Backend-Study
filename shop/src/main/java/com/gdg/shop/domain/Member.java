package com.gdg.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor
public class Member {

    // 회원 고유 식별자
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 회원 아이디
    @Column(name = "member_login_id", length = 50)
    private String loginId;

    // 비밀번호
    @Column(name = "member_pw", length = 100)
    private String password;

    // 전화번호
    @Column(name = "member_phone", length = 20)
    private String phoneNumber;

    // 주소
    @Column(name = "member_address", length = 250)
    private String address;

    // 적립금
    @Column(name = "member_point")
    private int point;

    /**
     * 회원 생성자 (id와 point는 자동 생성/초기화)
     */
    public Member(String loginId, String password, String phoneNumber, String address) {
        this.loginId = loginId;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.point = 0; // 신규 회원 적립금은 0으로 초기화
    }

    /**
     * 회원 정보 수정 메서드
     * - 비밀번호, 전화번호, 주소만 수정 가능
     * - loginId는 변경 불가
     */
    public void updateInfo(String password, String phoneNumber, String address) {
        if (password != null) {
            this.password = password;
        }
        if (phoneNumber != null) {
            this.phoneNumber = phoneNumber;
        }
        if (address != null) {
            this.address = address;
        }
    }
}

// [질문 정리 1] Member는 JPA Entity이므로 DB의 members 테이블과 매핑되는 도메인 객체이다.
// Entity는 API 요청/응답용 객체가 아니라 DB에 저장되는 핵심 데이터 구조에 가깝다.
//
// [질문 정리 2] @NoArgsConstructor는 JPA가 Entity 객체를 만들 때 필요한 기본 생성자를 만들어준다.
// 사용자가 직접 new Member(...)를 호출하지 않아도 JPA/Hibernate가 조회 결과를 Entity로 복원해야 하므로 기본 생성자가 필요하다.
//
// [질문 정리 3] @GeneratedValue(strategy = GenerationType.IDENTITY)는 id 생성을 DB에 맡기는 방식이다.
// 그래서 Hibernate insert 로그에서 id 값이 직접 들어가지 않고 default로 표시될 수 있다.
//
// [질문 정리 4] updateInfo처럼 의미 있는 변경 메서드를 두면 setter를 전부 열어두는 것보다 안전하다.
// 예를 들어 loginId는 회원 식별용 값이므로 수정 대상에서 제외하고, password/phoneNumber/address만 바꾸도록 제한할 수 있다.
