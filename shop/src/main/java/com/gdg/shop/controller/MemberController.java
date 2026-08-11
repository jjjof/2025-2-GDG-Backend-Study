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
// @RestController는 @Controller + @ResponseBody 역할을 한다.
// return 값을 뷰 이름으로 보지 않고 HTTP 응답 body로 변환해서 클라이언트에게 보낸다.
@RestController

// @RequiredArgsConstructor는 final 필드를 받는 생성자를 Lombok이 자동으로 만들어준다.
// 여기서는 MemberService를 생성자 주입으로 받게 해준다.
//@RequiredArgsConstructor

// 이 컨트롤러의 공통 URL을 /members로 묶는다.
// 아래 @GetMapping, @PostMapping 등은 전부 /members 뒤에 붙는 경로가 된다.
@ResponseBody
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    // 클라이언트가 POST /members 요청을 보내면 Spring이 이 메서드를 호출한다.
    // @RequestBody는 요청 body(JSON)를 Java 객체로 바꿔서 request 변수에 넣어준다.
    public ResponseEntity<Void> createMember(@RequestBody MemberCreateRequest request){
        Long memberId = memberService.createMember(request);

        // ResponseEntity<Void>의 Void는 응답 body가 없다는 뜻이다.
        // created(uri)는 201 Created 상태 코드와 Location 헤더를 만든다.
        // build()는 body 없이 응답 객체를 완성한다.
        return ResponseEntity.created(URI.create("/members" + memberId)).build();
    }

    @GetMapping
    // GET /members 요청이다. GET 조회 요청은 보통 요청 body를 받지 않는다.
    // ResponseEntity<List<Member>>는 응답 body 전체가 회원 목록 타입이라는 서버 내부의 타입 표시다.
    // HTTP에는 List<Member>라는 Java 타입명이 실려 가지 않고, 실제로는 JSON 배열로 변환되어 나간다.
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();

        // members는 서비스에서 가져온 회원 목록을 담은 변수다.
        // ok(members)는 200 OK 상태 코드와 members를 응답 body로 보내겠다는 뜻이다.
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{memberId}")
    // {memberId}는 URL 경로에서 값이 들어오는 자리다.
    // 예: GET /members/1 -> memberId에 1이 들어간다.
    // @PathVariable은 URL 경로의 값을 메서드 파라미터로 꺼내준다.
    public ResponseEntity<Member> getMembers(@PathVariable Long memberId) {
        Member member = memberService.getMemberById(memberId);

        // ResponseEntity<Member>는 응답 body로 Member 객체 하나를 보낸다는 뜻이다.
        return ResponseEntity.ok(member);
    }

    @PatchMapping("/{memberId}")
    // PATCH /members/{memberId} 요청이다.
    // memberId는 URL 경로에서 받고, 수정할 값들은 요청 body에서 받는다.
    public ResponseEntity<Void> updateMember(@PathVariable Long memberId,
                                             @RequestBody MemberUpdateRequest request){
        memberService.updateMember(memberId, request);

        // ok()만 호출하면 아직 body를 넣을 수 있는 builder 상태다.
        // build()까지 호출해야 body 없는 200 OK 응답이 완성된다.
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{memberId}")
    // DELETE /members/{memberId} 요청이다.
    // 삭제할 대상의 id는 URL 경로에서 @PathVariable로 받는다.
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId){
        memberService.deleteMember(memberId);

        // noContent().build()는 204 No Content 응답이다.
        // 삭제 성공처럼 응답 body를 굳이 보낼 필요가 없을 때 자주 쓴다.
        return ResponseEntity.noContent().build();
    }
}
