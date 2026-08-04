<img width="845" height="240" alt="Image" src="https://github.com/user-attachments/assets/8e3ab399-6e24-486f-9d58-d6cacbe344cc" />

***
상품 기능
- 상품 정보 등록: POST, /items
- 상품 목록 조회: GET, /items
- 개별 상품 정보 상세 조회: GET, /items{itemsID}
- 상품 정보 수정: PATCT, /items{itemsID}
- 상품 삭제: DELETE, /items{itemsID}

주문 기능
- 주문 정보 생성: POST, /orders
- 주문 목록 조회: GET, /orders
- 개별 주문 정보 상세 조회: GET, /orders{ordersID}
- 주문 취소: DELETE, /orders{ordersID}

***
 백엔드 정규 스터디 1주차 요약

주제: 웹의 소통 방식, HTTP와 REST API
1. 웹이란?
- 인터넷: 전 세계 컴퓨터를 연결하는 네트워크
- 웹: 인터넷 위에서 동작하는 서비스
- 클라이언트-서버 모델: 요청(request)과 응답(response)의 구조
- URL 구성요소: Scheme, Host, Port, Path, Query

2. HTTP (HyperText Transfer Protocol)
- 데이터 송수신 프로토콜
- 무상태성(Stateless), 비연결성(Connectionless)
- 주요 메서드: GET, POST, PUT, PATCH, DELETE
- 상태 코드: 200(성공), 201(생성), 400(잘못된 요청), 404(리소스 없음), 500(서버 오류)
프론트엔드와 백엔드
- 프론트엔드: 사용자 인터페이스(UI)
- 백엔드: 요청 처리, 비즈니스 로직, 데이터베이스 관리
- 대표 DBMS: MySQL, PostgreSQL, MongoDB

3. API와 REST API
- API: 프로그램 간 통신을 위한 규칙(약속)
- REST: HTTP를 효율적으로 사용하는 아키텍처 스타일
- 구성요소: Resource(URI), Verb(Method), Representation(JSON)
- REST API: HTTP의 모범적 사용을 구현한 API
RESTful API 예시
회원 등록: POST /members
회원 목록 조회: GET /members
회원 상세 조회: GET /members/{memberId}
회원 정보 수정: PATCH /members/{memberId}
회원 삭제: DELETE /members/{memberId}

4. Spring Boot
- Spring: 자바 기반 경량 애플리케이션 프레임워크- Spring Boot: 설정을 단순화하여 스프링을 쉽게 사용할 수 있게 한 도구
