package com.gdg.shop.service;

import com.gdg.shop.domain.Member;
import com.gdg.shop.dto.MemberCreateRequest;
import com.gdg.shop.dto.MemberUpdateRequest;
import com.gdg.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MemberServiceImple implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long createMember(MemberCreateRequest request){
        // 컨트롤러 -> 서비스 호출은 HTTP 요청이 아니라 Java 메서드 호출이다.
        // HTTP는 보통 클라이언트와 컨트롤러 사이에서만 오간다.
        Member existingMember = memberRepository.findByLoginId(request.getLoginId());

        if (existingMember != null){
            // RuntimeException은 너무 넓은 예외 타입이다.
            // 실무에서는 DuplicateLoginIdException처럼 상황을 드러내는 구체적인 예외가 더 좋다.
            throw new RuntimeException("이미 존재하는 로그인 아이디입니다: " + request.getLoginId());
        }

        Member member = new Member(
                request.getLoginId(),
                request.getPassword(),
                request.getPhoneNumber(),
                request.getAddress()
        );

        memberRepository.save(member);

        // 컨트롤러는 이 id로 201 Created의 Location 헤더를 만들 수 있다.
        return member.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> findAllMembers(){
        return memberRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Member getMemberById(Long id){
        Member member = memberRepository.findById(id);

        if (member == null) {
            // "회원을 찾을 수 없음"은 404 Not Found와 연결하기 좋은 비즈니스 예외다.
            // RuntimeException 대신 MemberNotFoundException 같은 별도 타입을 만들면 예외 처리가 명확해진다.
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        return member;
    }

    @Override
    @Transactional
    public void updateMember(Long id, MemberUpdateRequest request){
        Member member = memberRepository.findById(id);

        if (member == null) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        member.updateInfo(request.getPassword(), request.getPhoneNumber(), request.getAddress());
    }

    @Override
    @Transactional
    public void deleteMember(Long id){
        Member member = memberRepository.findById(id);

        if (member == null) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        memberRepository.deleteById(id);
    }
}

// [질문 정리 1] Service 계층은 Controller에서 받은 요청을 실제 비즈니스 로직으로 처리하는 곳이다.
// 회원 생성에서는 중복 loginId 확인, Member Entity 생성, Repository 저장 호출을 담당한다.
//
// [질문 정리 2] 회원 등록 시 Hibernate 로그에 select가 먼저 보이는 이유는 findByLoginId로 중복 회원을 확인하기 때문이다.
// 같은 loginId가 없을 때만 insert가 실행되므로 "select 후 insert" 흐름은 현재 로직상 정상이다.
//
// [질문 정리 3] new Member(...)를 호출할 때 생성자 매개변수 순서와 전달 값의 순서가 반드시 일치해야 한다.
// 순서가 어긋나면 컴파일 에러는 나지 않지만 phoneNumber와 address 같은 값이 서로 바뀌어 저장될 수 있다.
//
// [질문 정리 4] @Transactional은 하나의 서비스 작업을 트랜잭션 단위로 묶는다.
// 조회만 하는 메서드는 readOnly = true를 붙이면 변경 작업이 없다는 의도를 표현할 수 있다.
// @RequiredArgsConstructor는 final 필드를 받는 생성자를 자동 생성한다.
// Spring은 그 생성자를 통해 MemberRepository를 주입한다.