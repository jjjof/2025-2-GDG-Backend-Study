package com.gdg.shop.service;

import com.gdg.shop.domain.Member;
import com.gdg.shop.dto.MemberCreateRequest;
import com.gdg.shop.dto.MemberUpdateRequest;

import java.util.List;

public interface MemberService {
    Long createMember(MemberCreateRequest memberCreateRequest);
    List<Member> findAllMembers();
    Member getMemberById(Long id);
    void updateMember(Long id, MemberUpdateRequest memberUpdateRequest);
    void deleteMember(Long id);
}
