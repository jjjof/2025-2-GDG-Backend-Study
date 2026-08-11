package com.gdg.shop.service;

import com.gdg.shop.domain.Member;
import com.gdg.shop.dto.MemberCreateRequest;
import com.gdg.shop.dto.MemberUpdateRequest;
import com.gdg.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

// @RequiredArgsConstructor는 final 필드를 받는 생성자를 자동 생성한다.
// Spring은 그 생성자를 통해 MemberRepository를 주입한다.
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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
                request.getAddress(),
                request.getPhoneNumber()
        );

        memberRepository.save(member);

        // 컨트롤러는 이 id로 201 Created의 Location 헤더를 만들 수 있다.
        return member.getId();
    }

    @Transactional(readOnly = true)
    public List<Member> findAllMembers(){
        return memberRepository.findAll();
    }

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

    @Transactional
    public void updateMember(Long id, MemberUpdateRequest request){
        Member member = memberRepository.findById(id);

        if (member == null) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        member.updateInfo(request.getPassword(), request.getPhoneNumber(), request.getAddress());
    }

    @Transactional
    public void deleteMember(Long id){
        Member member = memberRepository.findById(id);

        if (member == null) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        memberRepository.deleteById(id);
    }
}
