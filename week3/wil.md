1. ﻿스프링 계층형 아키텍처 (Layered Architecture)
-Controller : ﻿클라이언트 요청을 받아 DTO로 서비스 계층에 전달
-Service : 비즈니스 로직 수행, Repository와 연동
-Repository(DAO) : DB 와 직접 통신하여 CURD 수행

2. ORM과 JPA
- ﻿ORM(Object-Relational Mapping): 객체와 데이터베이스 간 패러다임 불일치를 해결하고 CRUD를 자동화한다.
- ﻿JPA(Java Persistence API): Java 진영의 ORM 표준으로, Hibernate가 대표 구현체이다. SQL 없이 EntityManager로 등록·조회·수정·삭제 가능.

3. JPA의 동작 방식
- ﻿EntityManager: 엔티티를 관리하는 창구 역할
- P﻿ersistence Context: 엔티티의 가상 저장소
- ﻿상태 전이: 비영속 → 영속 → 준영속 → 삭제
주요 특징:
- 1차 캐시: 동일 트랜잭션 내 동일 객체 재사용
- 동일성 보장: 같은 엔티티는 같은 인스턴스로 반환
- 쓰기 지연: 커밋 시 SQL 일괄 실행
- 변경 감지: 자동 UPDATE 생성
- 트랜잭션 단위로 일관성 유지
트렌젝션 예시:
- 주문 저장, 재고 차감, 포인트 적립 모두 성공 → Commit
- 중간에 하나 실패 → Rollback

4. CRUD 동작 예시
- 등록(em.persist(entity)) : 영속성 컨텍스트에 저장 → 커밋 시 DB INSTERT
- 조회(em.find(Entity.class, id)) : 1차 캐시 → 없으면 DB SELECT
- 수정(앤티티 값 변경 후 커밋) : 변경 감지에 따라 UPDATE 자동 실행
- 삭제(em.remove(entity)) : 삭제 예약 후 커밋 시 DELETE 실행
