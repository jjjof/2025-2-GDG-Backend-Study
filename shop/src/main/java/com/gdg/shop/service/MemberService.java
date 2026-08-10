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
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long createMember(MemberCreateRequest request){
        Member existingMember = memberRepository.findByLoginId(request.getLoginId());

        if (existingMember != null){
            throw new RuntimeException("이미 존재하는 로그인 아이디입니다: " + request.getLoginId());
        }
        Member member = new Member(
                request.getLoginId(),
                request.getPassword(),
                request.getAddress(),
                request.getPhoneNumber()
        );

        memberRepository.save(member);

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
