# 백엔드 정규 스터디 4주차

> **주제:** ERD, DB, 엔티티  
> **내용:** 데이터베이스 모델링 → JPA 엔티티 구현 → Postman API 테스트

---

# 1. Layered Architecture 복습

Spring의 계층형 아키텍처는 크게 다음과 같은 흐름으로 동작한다.

```text
Client
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
Database
```

각 계층은 자신의 역할을 담당한다.

---

## 1.1 Controller Layer

**Controller = 웨이터**

클라이언트의 요청을 가장 먼저 받는 계층이다.

### 역할

- HTTP 요청 및 응답 처리
- 특정 Endpoint(URL)로 들어오는 요청을 처리
- DTO(Data Transfer Object)를 사용하여 Service 계층과 데이터 전달

```text
Client
   ↓ HTTP Request
Controller
   ↓ DTO
Service
```

---

## 1.2 Service Layer

**Service = 요리사**

애플리케이션의 **비즈니스 로직**이 들어가는 계층이다.

### 역할

- 비즈니스 로직 수행
- Controller와 Repository 사이의 중간 역할
- Repository와 Entity 또는 DTO를 이용하여 데이터 전달

```text
Controller
    ↓
 Service
    ↓
Repository
```

---

## 1.3 Repository Layer

데이터베이스에 접근하여 **CRUD**를 수행하는 계층이다.

### CRUD

| 기능 | 의미 |
|---|---|
| Create | 데이터 생성 |
| Read | 데이터 조회 |
| Update | 데이터 수정 |
| Delete | 데이터 삭제 |

```text
Service
   ↓
Repository
   ↓
Database
```

---

# 2. Entity와 Database

## Entity

**Entity = 원재료**

Entity는 데이터베이스 테이블과 매핑되는 핵심 객체이다.

예를 들어 상품이라는 Entity가 있다면 다음과 같은 모든 정보를 가지고 있을 수 있다.

- 상품명
- 가격
- 재고
- 원산지
- 등급
- 유통기한

### 특징

- DB 테이블과 매핑
- 애플리케이션의 핵심 데이터를 표현
- 외부에 직접 노출하지 않는 것이 좋음
  - 데이터 일관성
  - 보안

---

## Database

**DB = 냉장/냉동 창고**

Entity에 해당하는 데이터를 실제로 저장하는 공간이다.

---

# 3. ERD

## ERD란?

**ERD(Entity-Relationship Diagram)**

> 데이터베이스의 **청사진(blueprint)**

데이터베이스에서 어떤 데이터를 저장하고, 데이터들 사이에 어떤 관계가 있는지를 그림으로 나타낸 것이다.

---

## 3.1 Entity

**Entity(개체)**

관리해야 할 데이터의 주체이다.

### 예시

- 회원(Member)
- 상품(Product)
- 주문(Order)

---

## 3.2 Attribute

**Attribute(속성)**

각 Entity가 가지고 있는 구체적인 정보이다.

```text
Attribute = Field = Column
```

### Member

```text
Member
├── id
├── name
└── address
```

### Product

```text
Product
├── name
├── price
└── stock
```

---

# 4. Primary Key

## PK(Primary Key)

테이블의 각 데이터를 **고유하게 식별하기 위한 컬럼**이다.

### 예시

```text
Member
├── id          ← PK
├── name
└── address
```

테이블별로 다음과 같은 PK를 사용할 수 있다.

```text
Member  → member_id
Product → product_id
Order   → order_id
```

### 핵심

> PK는 각각의 데이터를 서로 구분할 수 있도록 해주는 고유 식별자이다.

---

# 5. Foreign Key

## FK(Foreign Key)

다른 테이블의 **Primary Key를 참조하는 컬럼**이다.

> 테이블 사이의 연결고리

---

## 예시

### Member

```text
member_id (PK)
```

### Order

```text
order_id  (PK)
member_id (FK)
```

예를 들어 100번 주문을 누가 했는지 저장하고 싶다고 하자.

```text
Member

member_id
---------
1
```

```text
Order

order_id | member_id
---------|----------
100      | 1
```

`member_id = 1`이라는 값을 이용하여

> **1번 회원이 100번 주문을 했다**

라는 관계를 표현할 수 있다.

---

# 6. Relation

## 관계(Relation)

Entity 사이의 연관성 또는 업무 규칙을 의미한다.

관계는 보통 **외래 키(FK)** 또는 **별도의 테이블**을 이용하여 구현한다.

### 관계 종류

| 관계 | 표현 |
|---|---|
| 다대일 | N : 1 |
| 일대다 | 1 : N |
| 일대일 | 1 : 1 |
| 다대다 | N : M |

---

# 7. 일대다 관계

## 1 : N

예를 들어

> 한 명의 회원은 여러 개의 주문을 할 수 있다.

```text
Member (1) : (N) Order
```

예시:

```text
Member
member_id = 1
```

```text
Order

order_id | member_id
---------|----------
101      | 1
102      | 1
103      | 1
```

회원 한 명이 여러 주문을 가지고 있으므로

```text
Member 1 : N Order
```

관계가 된다.

### FK 위치

`Order` 테이블이 `member_id`를 FK로 가진다.

```text
Member
 └── member_id (PK)
       ↑
       │
Order
 └── member_id (FK)
```

> 1:N 관계는 보통 **N쪽에서 FK를 가진다.**

---

# 8. 다대다 관계

## N : M

예시:

```text
Student
Course
```

한 명의 학생은 여러 개의 강의를 들을 수 있다.

```text
철수
├── 컴퓨터 개론
└── 자료구조
```

하나의 강의 역시 여러 명의 학생이 들을 수 있다.

```text
컴퓨터 개론
├── 철수
└── 영희
```

따라서

```text
Student N : M Course
```

관계이다.

---

# 9. N:M을 FK 하나로 해결하면 안 되는 이유

다음 방법은 적절하지 않다.

```text
Student
└── course_id (FK)
```

이렇게 하면 학생 한 명당 하나의 강의만 저장할 수 있다.

반대로

```text
Course
└── student_id (FK)
```

로 만들면 강의 하나당 학생 한 명만 저장할 수 있게 된다.

즉, **단순 FK 하나만으로 N:M 관계를 제대로 표현하기 어렵다.**

---

# 10. N:M 해결 방법

## 중간 테이블(연결 Entity) 사용

Student와 Course 사이에 새로운 Entity를 만든다.

```text
Student
   │
 1 │
   │ N
Enrollment
   │ N
   │
 1 │
Course
```

즉,

```text
Student 1 : N Enrollment
Enrollment N : 1 Course
```

형태로 변경한다.

---

## Enrollment

수강 신청 정보를 저장하는 연결 테이블이다.

```text
Enrollment
├── enrollment_id (PK)
├── student_id    (FK)
├── course_id     (FK)
└── grade
```

예시:

| enrollment_id | student_id | course_id |
|---|---:|---:|
| 1001 | 1 | 501 |
| 1002 | 1 | 502 |
| 1003 | 2 | 501 |

이를 통해

```text
철수 → 컴퓨터 개론
철수 → 자료구조
영희 → 컴퓨터 개론
```

과 같은 관계를 표현할 수 있다.

### 핵심

> N:M 관계는 **중간 테이블(연결 Entity)**을 도입하여 두 개의 1:N 관계로 풀어낸다.

---

# 11. ERD를 통한 DB 설계

예제 시스템:

> 쇼핑몰 Admin

필요한 기능은 다음과 같다.

## 회원

- 회원 등록
- 회원 조회
- 회원 수정
- 회원 삭제

## 상품

- 상품 등록
- 상품 조회
- 상품 수정
- 상품 삭제

## 주문

- 상품 주문
- 주문 내역 조회
- 주문 취소

이 요구사항을 분석하여

```text
Member
Product
Order
```

등의 Entity를 정의하고 관계를 설정한다.

ERD 작성에는 **ERD Cloud** 등의 도구를 사용할 수 있다.

---

# 12. 식별 관계와 비식별 관계

## 식별 관계

**강한 연관 관계**

관계 대상 Entity의 PK를 자신의 **PK로도 사용**한다.

```text
Parent
PK : parent_id
```

```text
Child
PK : parent_id
```

---

## 비식별 관계

**느슨한 연관 관계**

관계 대상의 PK를 자신의 **FK로만 사용**한다.

```text
Parent
PK : parent_id
```

```text
Child
PK : child_id
FK : parent_id
```

일반적으로는 **비식별 관계를 많이 선택한다.**

---

# 13. ORM

## ORM(Object-Relational Mapping)

객체와 관계형 데이터베이스 데이터를 자동으로 매핑해주는 기술이다.

```text
Java Object
     ↕
    ORM
     ↕
Database
```

### ORM의 역할

- 객체와 관계형 DB 사이의 패러다임 차이를 해결
- 객체와 테이블을 매핑
- 반복적인 SQL 작성 감소
- CRUD 작업을 보다 편리하게 처리

---

# 14. JPA

## JPA(Java Persistence API)

Java 진영의 **ORM 기술 표준**이다.

Java 객체를 데이터베이스에 저장하고 관리하기 위한 인터페이스와 기능을 제공한다.

대표적인 구현체:

```text
Hibernate
```

관계는 다음과 같다.

```text
JPA
 ↓
Hibernate
 ↓
Database
```

---

## JPA의 역할

Entity 클래스를 정의하면 JPA가 해당 클래스를 분석한다.

```text
Entity Class
      ↓
     JPA
      ↓
CREATE TABLE ...
      ↓
 Database
```

즉,

> Java Entity 클래스의 정의를 참고하여 테이블 생성 SQL 등을 생성하고 실행한다.

---

# 15. H2 Database

## H2

Java로 작성된 관계형 데이터베이스이다.

### 특징

- 가벼움
- 빠름
- 별도 설치 없이 사용 가능
- 개발 및 테스트 환경에서 편리

---

# 16. JPA와 H2 의존성 추가

`build.gradle`

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'

    runtimeOnly 'com.h2database:h2'
}
```

의존성을 수정했다면 Gradle을 다시 로드한다.

IntelliJ에서는 코끼리 모양의 **Gradle Load 버튼**을 사용할 수 있다.

---

# 17. application.yml 설정

기존

```text
application.properties
```

대신

```text
application.yml
```

형식을 사용할 수 있다.

---

## DB 설정

```yaml
spring:
  application:
    name: shop

  datasource:
    url: jdbc:h2:mem:shop;MODE=MYSQL

  h2:
    console:
      enabled: true
```

### datasource

```yaml
url: jdbc:h2:mem:shop;MODE=MYSQL
```

- 메모리 기반 H2 DB 사용
- DB 이름: `shop`
- `MODE=MYSQL`
  - H2가 MySQL과 유사하게 동작하도록 설정

---

## H2 Console

```yaml
h2:
  console:
    enabled: true
```

H2 관리자 콘솔을 활성화한다.

기본값은 `false`이다.

---

# 18. JPA SQL 설정

```yaml
spring:
  jpa:
    show-sql: true

    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQL8Dialect
```

## show-sql

```yaml
show-sql: true
```

JPA가 생성한 SQL을 콘솔에 출력한다.

---

## format_sql

```yaml
format_sql: true
```

SQL을 보기 좋게 들여쓰기해서 출력한다.

---

## dialect

```yaml
dialect: org.hibernate.dialect.MySQL8Dialect
```

SQL을 생성할 때 MySQL 8 문법을 사용하도록 지정한다.

---

# 19. H2 Console 접속

Spring 애플리케이션 실행 후

```text
http://localhost:8080/h2-console
```

에 접속한다.

JDBC URL에는 다음을 입력한다.

```text
jdbc:h2:mem:shop
```

이후 `Connect`를 누른다.

---

# 20. Entity Class

JPA에서 사용하는 클래스를 Entity로 선언하려면 다음 어노테이션을 사용한다.

```java
@Entity
```

PK는 다음과 같이 지정한다.

```java
@Id
```

예시:

```java
@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_login_id", length = 50)
    private String loginId;

    @Column(name = "member_pw", length = 100)
    private String password;
}
```

---

# 21. @GeneratedValue

PK를 자동 생성하려면 다음을 사용한다.

```java
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

`IDENTITY` 전략은

> PK 값 생성을 DB에게 위임하는 방식

이다.

예:

```text
1
2
3
4
...
```

---

# 22. @Column

테이블의 Column 정보를 지정할 수 있다.

```java
@Column(
    name = "member_login_id",
    length = 50
)
private String loginId;
```

주로 다음과 같은 정보를 설정한다.

- Column 이름
- 길이
- 기타 Column 설정

---

# 23. JPA에서 FK 표현하기

객체에서는 FK 값을 직접 저장하기보다 **연결된 Entity 객체를 필드로 선언**할 수 있다.

예:

```java
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
```

DB에서는

```text
member_id
```

라는 FK 컬럼으로 저장되지만 Java 코드에서는

```java
private Member member;
```

처럼 객체 관계로 표현한다.

> 객체의 관계를 DB의 FK 관계로 변환하는 것이 ORM의 역할이다.

---

# 24. @JoinColumn

```java
@JoinColumn(name = "member_id")
```

FK Column 정보를 지정한다.

예를 들어

```java
@JoinColumn(name = "member_id")
```

이면 해당 연관관계가 DB의 `member_id` FK 컬럼을 사용한다는 의미이다.

---

# 25. 연관관계 어노테이션

JPA에서는 관계의 종류에 따라 다음 어노테이션을 사용한다.

| 관계 | Annotation |
|---|---|
| N : 1 | `@ManyToOne` |
| 1 : N | `@OneToMany` |
| 1 : 1 | `@OneToOne` |
| N : M | `@ManyToMany` |

예:

```java
@ManyToOne
@JoinColumn(name = "member_id")
private Member member;
```

특히 다음 두 개를 기억한다.

```java
@ManyToOne
@JoinColumn(...)
```

---

# 26. FetchType

연관된 Entity를 언제 가져올지 결정한다.

---

## EAGER

**즉시 로딩**

Order를 조회하는 순간 연결되어 있는 Member 데이터까지 함께 가져온다.

```text
Order 조회
   ↓
Order + Member 조회
```

---

## LAZY

**지연 로딩**

Order를 조회할 때 Member 데이터를 바로 가져오지 않고 실제로 필요한 시점에 가져온다.

```text
Order 조회
   ↓
Order만 조회

Member 필요
   ↓
Member 조회
```

자료에서는 `@ManyToOne` 사용 시 다음과 같이 `LAZY`를 지정한다.

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "member_id")
private Member member;
```

---

# 27. Entity 생성자

Entity는 일반적으로 **id를 제외한 필드들로 생성**한다.

그리고 JPA가 Entity를 사용하려면

> 인자가 없는 기본 생성자

가 필요하다.

Lombok을 사용하면 다음과 같이 만들 수 있다.

```java
@NoArgsConstructor(access = AccessLevel.PROTECTED)
```

### protected를 사용하는 이유

```text
JPA → 생성자 사용 가능
외부 코드 → 무분별한 생성 방지
```

---

# 28. @Getter

Entity에 다음을 붙이면

```java
@Getter
```

모든 필드의 Getter가 생성된다.

예:

```java
member.getId();
member.getLoginId();
```

---

# 29. Entity 구현 핵심 정리

```java
@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
```

핵심 Annotation:

```text
@Entity
@Id
@GeneratedValue
@Column
@JoinColumn
@ManyToOne
@NoArgsConstructor
@Getter
```

---

# 30. Postman

프론트엔드 화면이 아직 없어도 API 자체는 구현할 수 있다.

예:

```text
회원 등록 API
상품 등록 API
상품 조회 API
주문 API
```

하지만 웹 페이지가 없다면 직접 테스트하기 어렵다.

이때 **Postman**을 사용한다.

---

## Postman이 하는 일

HTTP 요청을 직접 전송하여 API를 테스트할 수 있다.

### 테스트 가능

- Create
- Read
- Update
- Delete

즉,

```text
CRUD API 테스트
```

가 가능하다.

공식 사이트:

```text
https://www.postman.com/
```

Desktop App 설치 권장.

---

# 31. 전체 흐름

이번 주차의 핵심 흐름은 다음과 같다.

```text
1. 요구사항 분석

      ↓

2. Entity 정의

Member
Product
Order

      ↓

3. 관계 분석

1:N
N:M

      ↓

4. ERD 작성

      ↓

5. JPA Entity Class 구현

@Entity
@Id
@ManyToOne
@JoinColumn
...

      ↓

6. JPA가 DB Table 생성

      ↓

7. H2 Console에서 확인

      ↓

8. API 구현

      ↓

9. Postman으로 테스트
```

---

# 32. 과제

## 1. Entity 구현

다음 세 Entity를 구현한다.

```text
Member
Product
Order
```

---

## 2. `wil.md` 작성

이번 주차 학습 내용을 정리한

```text
wil.md
```

파일을 작성한다.

---

## 3. 이미지 첨부

`wil.md`에 다음 이미지들을 첨부한다.

### ① DB ERD

ERD Cloud 또는 다른 ERD 도구를 사용하여 DB 구조를 작성한다.

```text
ERD Screenshot
```

---

### ② H2 Table

Spring Application 실행 후 JPA가 생성한 H2 Table을 확인한다.

```text
H2 Table Screenshot
```

---

### ③ Postman API 테스트

Member를 제외한 다음 Domain 중 하나를 선택한다.

```text
Product
Order
```

해당 Domain의 CRUD API를 테스트한다.

첨부할 이미지:

```text
성공 Case 1장
+
실패 Case 1장
```

---

# 33. 쇼핑몰 기능 명세

## Member

```text
회원 등록
회원 조회
회원 수정
회원 삭제
```

## Product

```text
상품 등록
상품 조회
상품 수정
상품 삭제
```

## Order

```text
상품 주문
주문 내역 조회
주문 취소
```

그 외 언급되지 않은 세부 명세는 자유롭게 구성한다.

---

# 34. 과제에서 중요한 N:M 관계

주의해야 할 관계:

```text
Order N : M Product
```

### 이유

주문 한 건에는 여러 상품이 포함될 수 있다.

```text
Order #1
├── Product A
├── Product B
└── Product C
```

반대로 상품 하나도 여러 주문에 포함될 수 있다.

```text
Product A
├── Order #1
├── Order #2
└── Order #3
```

따라서

```text
Order N : M Product
```

이다.

---

## N:M 관계 처리

앞에서 Student와 Course의 관계를 다음과 같이 해결했다.

```text
Student
   ↓
Enrollment
   ↓
Course
```

마찬가지로 Order와 Product 사이에도 **중간 Entity**가 필요하다는 점을 고려해야 한다.

개념적으로는 다음 형태가 된다.

```text
Order
  │
  │ 1:N
  ↓
중간 Entity
  ↑
  │ N:1
  │
Product
```

---

# 35. 핵심 암기

## DB

```text
Entity   = 관리해야 할 데이터의 주체
Attribute = Entity의 구체적인 정보
PK       = 데이터를 고유하게 식별
FK       = 다른 Table의 PK 참조
```

---

## 관계

```text
1 : N → N쪽에 FK
N : M → 중간 Table을 만들어 두 개의 1:N 관계로 변환
```

---

## JPA

```text
@Entity
@Id
@GeneratedValue
@Column

@ManyToOne
@JoinColumn

FetchType.LAZY
```

---

## 전체 구조

```text
ERD
 ↓
Entity Class
 ↓
JPA
 ↓
Database Table
 ↓
Repository
 ↓
Service
 ↓
Controller
 ↓
API
 ↓
Postman Test
```

---

# 36. 한 줄 요약

> **ERD로 데이터베이스 구조를 설계하고, JPA Entity로 이를 코드에 구현한 뒤, 생성된 DB와 API를 H2 및 Postman으로 검증한다.**

---

# 37. 추가 질문 정리

## 37.1 yml과 properties 차이

`application.properties`는 `key=value` 형태로 설정을 작성한다.

```properties
server.port=8080
spring.datasource.url=jdbc:h2:mem:shop
```

`application.yml`은 들여쓰기 기반으로 계층 구조를 표현한다.

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:shop
```

둘 다 Spring Boot 설정 파일로 사용할 수 있다. 설정이 적으면 properties도 괜찮고, 계층이 깊거나 설정이 많아지면 yml이 읽기 편하다.

---

## 37.2 IntelliJ 생성자 단축키

IntelliJ IDEA에서 생성자, getter, setter 등을 자동 생성하려면 Windows 기준 `Alt + Insert`를 사용한다.

생성자 생성 화면에서 여러 필드를 한 번에 선택하려면 `Shift + 방향키` 또는 `Shift + 클릭`을 사용한다.

---

## 37.3 H2 Console과 h2.bat 차이

`http://localhost:8080/h2-console`은 Spring Boot 애플리케이션이 제공하는 H2 콘솔이다.

Spring Boot가 실행 중일 때 애플리케이션이 사용하는 H2 DB를 확인하기 좋다.

반면 `h2.bat`로 실행하는 콘솔은 H2 프로그램을 독립적으로 실행하는 방식이다.

메모리 DB를 사용할 때는 JDBC URL이 조금만 달라도 다른 DB를 보는 상황이 생길 수 있으므로, 강의처럼 Spring Boot의 `/h2-console`을 사용하는 편이 더 단순하다.

---

## 37.4 Postman을 사용하는 이유

Postman은 HTTP 요청을 직접 만들어 서버에 보내는 API 테스트 도구이다.

브라우저 주소창은 보통 GET 요청은 쉽게 보낼 수 있지만, POST/PATCH/DELETE 요청이나 JSON body, header 설정은 불편하다.

Postman을 사용하면 프론트엔드 화면이 없어도 백엔드 API를 직접 테스트할 수 있다.

예시 흐름:

```text
POST /members       회원 등록
GET /members        회원 목록 조회
GET /members/1      회원 단건 조회
PATCH /members/1    회원 수정
DELETE /members/1   회원 삭제
```

---

## 37.5 Postman에서 415 Unsupported Media Type이 나는 이유

Spring Controller에서 `@RequestBody`로 JSON을 받는 경우 요청의 `Content-Type`이 `application/json`이어야 한다.

Postman Body에서 `raw`를 선택한 뒤 오른쪽 타입을 `Text`로 두면 `text/plain`으로 전송될 수 있다.

이 경우 Spring은 JSON 요청으로 해석하지 못해서 `415 Unsupported Media Type`을 응답할 수 있다.

해결 방법:

```text
Body -> raw -> JSON 선택
```

---

## 37.6 201 Created 응답의 의미

`POST /members` 요청 후 `201 Created`가 나오면 새 회원 리소스가 생성되었다는 뜻이다.

컨트롤러에서 `ResponseEntity.created(...).build()`를 사용하면 응답 body는 비어 있을 수 있다.

body가 없더라도 상태 코드가 `201 Created`라면 생성 요청 자체는 성공한 것이다.

---

## 37.7 Hibernate SQL 로그 해석

회원 등록 시 다음과 같은 흐름의 SQL이 보일 수 있다.

```text
select ... from members where member_login_id=?
insert into members (...) values (...)
```

첫 번째 select는 중복 loginId가 있는지 확인하는 쿼리이다.

두 번째 insert는 실제 회원을 저장하는 쿼리이다.

`?`는 실제 값이 들어갈 자리이고, 로그에서는 값이 숨겨져 표시된다.

`id`가 `default`로 표시되는 것은 `@GeneratedValue(strategy = GenerationType.IDENTITY)` 때문에 DB가 id 생성을 맡기기 때문이다.

---

## 37.8 Postman 폴더와 Git

Postman 앱이 Git 저장소 안에 `.postman/` 또는 `postman/` 폴더를 만들 수 있다.

`git status`에서 다음처럼 보이면 아직 Git이 추적하지 않는 새 파일이다.

```text
?? .postman/
?? postman/
```

이 상태에서 `git add .`를 실행하면 해당 폴더도 포함된다.

Postman 컬렉션을 팀과 공유하려는 목적이면 올릴 수 있지만, 개인 워크스페이스 정보나 토큰이 들어갈 가능성이 있으면 `.gitignore`에 추가하는 것이 좋다.

```gitignore
.postman/
postman/
```
