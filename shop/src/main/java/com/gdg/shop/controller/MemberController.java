package com.gdg.shop.controller;

import com.gdg.shop.domain.Member;
import com.gdg.shop.dto.MemberCreateRequest;
import com.gdg.shop.dto.MemberUpdateRequest;
import com.gdg.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

//@Controller
//@RequiredArgsConstructor //private final 같이 초기화되어야 하는 변수를 초기화하는 생성자를 만들어줌
@RestController //위 두 어노테이션 합친 역할
@ResponseBody
@RequestMapping("/members") //중복되는 URL 제거
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody MemberCreateRequest request){
        Long memberId = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members" + memberId)).build();
    }

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<Member> getMembers(@PathVariable Long memberId) {
        Member member = memberService.getMemberById(memberId);
        return ResponseEntity.ok(member);

    }
    @PatchMapping("/{memberId}")
    public ResponseEntity<Void> updateMember(@PathVariable Long memberId,
                                             @RequestBody MemberUpdateRequest request){
        memberService.updateMember(memberId, request);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId){
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }

}
