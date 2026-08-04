백엔드 정규 스터디 2주차 요약

1. 계층형 아키텍쳐(Layered Architecture)
- 계층형 아케텍쳐란 역할별로 기능을 분리한 구조로, 유지보수성과 확장성을 높인다.
- Controller → Service → Repository → DB 순으로 요청이 전달된다.
- Controller: 요청 수신 및 응답 반환 (웨이터)
- Service: 비즈니스 로직 수행 (주방장)
- Repository(DAO): 데이터 접근 관리 (창고 관리자)
- DB: 실제 데이터 저장 (냉장고)
- DTO (Data Transfer Object): 통신용 객체, 필요한 정보만 전송 (주문서, 영수증)
- Entity: DB 테이블과 매핑된 핵심 객체, 외부 직접 노출 금지
- DTO는 필요한 정보만, Entity는 모든 속성을 보유하여 데이터 일관성과 보안을 유지한다.
2. 컨트롤러 계층(Controller Layer)
- HTTP 요청을 가장 먼저 처리하는 진입점
- DTO를 통해 Service와 데이터 교환
- 주요 어노테이션: @Controller, @RestController, @RequestMapping, @RequestBody
- 요청/응답 데이터는 JSON 포맷 사용상태 코드 예시:200 OK, 201 Created, 204 No Content, 400 Bad Request, 404 Not Found, 500 Internal Server Error

패키지 구조:
- 계층형 구조: 기능별 폴더 분리 (controller, service, repository)
- 도메인형 구조: 도메인 단위로 통합 관리 → 유지보수 유리

4. 서비스 계층(Service Layer)
- 비즈니스 로직의 중심
- Controller와 Repository를 연결하는 중간 계층
- 트랜잭션 단위로 로직을 처리해 원자성(atomicity) 보장
- 예시: 회원 정보 수정, 결제, 주문, 재고 차감 등
2주차 실습 한계:
- DB 대신 메모리 구조 사용
- @Transactional 비활성화 (주석 처리)
- JPA는 3주차에서 본격 도입
3. 스프링 빈 & 의존성 주입(Bean & DI)
스프링 빈(Spring Bean):
- 스프링 컨테이너(Application Context)에 등록된 공용 객체
- 필요한 객체를 직접 생성하지 않고 컨테이너에서 주입받음
- 등록 방식: 자동(@Component, @Service, @Controller, @Repository), 수동(설정 파일)
컴포넌트 스캔:
- @SpringBootApplication 안에 @ComponentScan 포함
- 스프링이 자동으로 Bean 탐색 및 등록
의존성 주입(DI):
- “내가 의존하는 객체를 직접 생성하지 않고 외부에서 주입받는다.”
- 장점: 메모리 효율적, 결합도 낮음, 테스트 용이
DI 방식:
- 생성자 주입: 가장 권장됨 (@Autowired, @RequiredArgsConstructor)
- 필드 주입: 간단하지만 테스트 어려움
- Setter 주입: 런타임 변경 가능, 불안정



