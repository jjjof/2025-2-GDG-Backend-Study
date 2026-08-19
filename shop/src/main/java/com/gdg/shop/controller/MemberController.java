package com.gdg.shop.controller;

import com.gdg.shop.domain.Member;
import com.gdg.shop.dto.MemberCreateRequest;
import com.gdg.shop.dto.MemberUpdateRequest;
import com.gdg.shop.service.MemberService;
import com.gdg.shop.service.MemberServiceImple;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@ResponseBody
@Tag(name = "회원 관리", description = "회원 CRUD API")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @Operation(summary = "회원 생성", description = "새로운 회원을 등록합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검사 실패 또는 중복된 로그인 아이디)")
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberCreateRequest request){
        Long memberId = memberService.createMember(request);

        return ResponseEntity.created(URI.create("/members/" + memberId)).build();
    }

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.findAllMembers();

        return ResponseEntity.ok(members);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<Member> getMembers(@PathVariable Long memberId) {
        Member member = memberService.getMemberById(memberId);

        return ResponseEntity.ok(member);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<Void> updateMember(@PathVariable Long memberId,
                                             @RequestBody @Valid MemberUpdateRequest request){
        memberService.updateMember(memberId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId){
        memberService.deleteMember(memberId);

        return ResponseEntity.noContent().build();
    }
}

// @RestController는 @Controller + @ResponseBody 역할을 한다.
// @RequiredArgsConstructor는 final 필드를 받는 생성자를 Lombok이 자동으로 만들어준다.
// 여기서는 MemberService를 생성자 주입으로 받게 해준다.
// 이 컨트롤러의 공통 URL을 /members로 묶는다.
// 아래 @GetMapping, @PostMapping 등은 전부 /members 뒤에 붙는 경로가 된다.
// return 값을 뷰 이름으로 보지 않고 HTTP 응답 body로 변환해서 클라이언트에게 보낸다.
// 클라이언트가 POST /members 요청을 보내면 Spring이 이 메서드를 호출한다.
// @RequestBody는 요청 body(JSON)를 Java 객체로 바꿔서 request 변수에 넣어준다.
// ResponseEntity<Void>의 Void는 응답 body가 없다는 뜻이다.
// created(uri)는 201 Created 상태 코드와 Location 헤더를 만든다.
// build()는 body 없이 응답 객체를 완성한다.
// GET /members 요청이다. GET 조회 요청은 보통 요청 body를 받지 않는다.
// ResponseEntity<List<Member>>는 응답 body 전체가 회원 목록 타입이라는 서버 내부의 타입 표시다.
// HTTP에는 List<Member>라는 Java 타입명이 실려 가지 않고, 실제로는 JSON 배열로 변환되어 나간다.
// members는 서비스에서 가져온 회원 목록을 담은 변수다.
// ok(members)는 200 OK 상태 코드와 members를 응답 body로 보내겠다는 뜻이다.
// {memberId}는 URL 경로에서 값이 들어오는 자리다.
// 예: GET /members/1 -> memberId에 1이 들어간다.
// @PathVariable은 URL 경로의 값을 메서드 파라미터로 꺼내준다.
// ResponseEntity<Member>는 응답 body로 Member 객체 하나를 보낸다는 뜻이다.
// PATCH /members/{memberId} 요청이다.
// memberId는 URL 경로에서 받고, 수정할 값들은 요청 body에서 받는다.
// ok()만 호출하면 아직 body를 넣을 수 있는 builder 상태다.
// build()까지 호출해야 body 없는 200 OK 응답이 완성된다.
// DELETE /members/{memberId} 요청이다.
// 삭제할 대상의 id는 URL 경로에서 @PathVariable로 받는다.
// noContent().build()는 204 No Content 응답이다.
// 삭제 성공처럼 응답 body를 굳이 보낼 필요가 없을 때 자주 쓴다.
// [질문 정리 1] Controller는 HTTP 요청이 처음 들어오는 입구이다.
// /members로 들어오는 POST, GET, PATCH, DELETE 요청을 각각 Java 메서드에 연결한다.
//
// [질문 정리 2] @RequestBody는 요청 body의 JSON을 MemberCreateRequest나 MemberUpdateRequest 같은 DTO 객체로 변환한다.
// Postman에서 raw + JSON을 선택해야 Content-Type이 application/json이 되어 Spring이 JSON으로 해석할 수 있다.
// raw + Text 상태로 보내면 Content-Type이 text/plain이 되어 415 Unsupported Media Type이 발생할 수 있다.
//
// [질문 정리 3] ResponseEntity.created(...).build()는 201 Created 응답을 만든다.
// build()만 호출하면 응답 body는 비어 있고, 새로 생성된 리소스 위치는 Location 헤더로 알려주는 구조가 된다.
//
// [질문 정리 4] Entity인 Member를 그대로 응답하면 password 같은 내부 필드가 JSON에 포함될 수 있다.
// 실무에서는 조회 응답용 DTO를 따로 만들어 필요한 값만 내려보내는 방식이 더 안전하다.
