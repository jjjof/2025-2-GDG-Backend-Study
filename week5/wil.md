# 백엔드 정규 스터디 5주차

> **주제:** SOLID 원칙과 스프링 빈  
> **내용:** 객체 지향 프로그래밍 → SOLID 원칙 → IoC/DI → Spring Bean → 의존성 주입 방식

---

# 1. 객체 지향이란?

Java는 객체 지향 언어이고, Spring은 Java 기반 프레임워크이다.

Spring은 Java의 핵심 특징인 **객체 지향**을 잘 살려서 유연하고 변경에 강한 프로그램을 만들 수 있도록 도와주는 프레임워크이다.

---

## 1.1 객체 지향 프로그래밍

**OOP(Object-Oriented Programming)**는 프로그램을 여러 독립적인 부품, 즉 **객체들의 상호작용**으로 바라보는 프로그래밍 방식이다.

```text
프로그램
  ↓
여러 객체의 조합
  ↓
객체들이 서로 협력하여 기능 수행
```

객체 지향 프로그래밍은 프로그램을 작은 단위로 나누어 조립하듯 구성하기 때문에, 유연하고 변경이 쉬운 구조를 만들기 좋다.

---

# 2. 객체 지향의 네 가지 특징

## 2.1 추상화

객체의 공통적인 속성과 기능을 뽑아 정의하는 것이다.

예를 들어 자동차마다 세부 구현은 다르지만, 모두 다음과 같은 공통 기능을 가질 수 있다.

```text
자동차
├── 출발한다
├── 멈춘다
└── 방향을 바꾼다
```

---

## 2.2 캡슐화

서로 연관 있는 속성과 기능을 하나로 묶고, 데이터를 외부로부터 보호하는 것이다.

객체 내부의 세부 구현을 숨기고 필요한 기능만 외부에 제공하면, 외부 코드는 객체 내부 구조를 몰라도 사용할 수 있다.

---

## 2.3 상속

기존 클래스의 속성과 기능을 새로운 클래스가 물려받아 재사용하는 것이다.

공통 기능을 부모 클래스에 두고, 자식 클래스가 이를 확장하거나 재사용할 수 있다.

---

## 2.4 다형성

어떤 객체의 속성이나 기능이 상황에 따라 여러 형태를 가질 수 있는 성질이다.

쉽게 말하면,

> **하나의 역할에 대해 여러 구현 방식이 존재할 수 있는 것**

이다.

---

# 3. 다형성

## 3.1 역할과 구현

다형성에서 중요한 것은 **역할**과 **구현**을 분리하는 것이다.

```text
역할     → 인터페이스
구현     → 인터페이스를 구현한 클래스
```

예를 들어 자동차라는 역할이 있고, 실제 구현체로 BMW, 포르쉐 등이 있을 수 있다.

```text
자동차 역할
├── BMW
└── Porsche
```

운전자는 자동차의 역할만 알면 된다. 자동차 내부가 어떻게 구현되어 있는지 알 필요가 없다.

---

## 3.2 다형성의 장점

객체를 사용하는 쪽, 즉 클라이언트는 대상의 **역할**만 알면 된다.

### 장점

- 실제 구현이 어떻게 동작하는지 몰라도 됨
- 구현 대상의 내부 구조가 바뀌어도 영향이 적음
- 구현체 자체가 변경되어도 클라이언트 코드를 유지하기 쉬움
- 프로그램을 유연하고 변경에 용이하게 만들 수 있음

---

## 3.3 다형성을 적용하기 좋은 상황

다음처럼 구현 방식이 바뀔 가능성이 있다면 역할을 먼저 정의하고 구현체를 갈아 끼울 수 있게 설계하는 것이 좋다.

- 데이터 저장소의 종류가 바뀔 수 있는 경우
- 주문 금액 할인 정책이 여러 종류인 경우
- 회원 포인트 적립 정책이 추후 변경될 수 있는 경우

예시:

```java
public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
}
```

```java
public class MemoryMemberRepository implements MemberRepository {
}
```

```java
public class JpaMemberRepository implements MemberRepository {
}
```

---

# 4. SOLID 원칙

**SOLID**는 좋은 객체 지향 설계를 위한 5가지 원칙이다.

| 원칙 | 이름 | 의미 |
|---|---|---|
| SRP | 단일 책임 원칙 | 하나의 클래스는 하나의 책임만 가져야 한다 |
| OCP | 개방-폐쇄 원칙 | 확장에는 열려 있고 수정에는 닫혀 있어야 한다 |
| LSP | 리스코프 치환 원칙 | 자식 클래스는 부모 클래스를 대체할 수 있어야 한다 |
| ISP | 인터페이스 분리 원칙 | 인터페이스는 목적에 맞게 작게 분리해야 한다 |
| DIP | 의존관계 역전 원칙 | 구체 클래스보다 추상화에 의존해야 한다 |

---

# 5. SRP: 단일 책임 원칙

**Single Responsibility Principle**

하나의 클래스는 단 하나의 책임만 가져야 한다.

```text
클래스를 변경하는 이유는 단 하나여야 한다.
```

하나의 클래스가 여러 기능을 동시에 담당하면, 특정 기능을 수정할 때 다른 기능에도 영향을 줄 수 있다.

### 목적

- 유지보수성 향상
- 변경 범위 축소
- 코드 이해도 향상

예를 들어 `MemberService`가 회원 가입, 주문 생성, 상품 수정까지 모두 담당한다면 책임이 너무 많다.

```text
MemberService
├── 회원 가입
├── 회원 조회
├── 주문 생성
└── 상품 수정
```

이 경우 주문과 상품 관련 기능은 별도의 Service로 분리하는 것이 좋다.

---

# 6. OCP: 개방-폐쇄 원칙

**Open-Closed Principle**

클래스는 확장에는 열려 있어야 하고, 수정에는 닫혀 있어야 한다.

즉,

```text
새 기능을 추가할 수는 있어야 하지만,
기존 코드는 최대한 수정하지 않아야 한다.
```

예를 들어 회원 저장소를 메모리 저장소에서 JPA 저장소로 바꿀 때 `MemberService` 코드를 직접 수정해야 한다면 OCP를 지키기 어렵다.

---

# 7. LSP: 리스코프 치환 원칙

**Liskov Substitution Principle**

자식 클래스는 언제나 부모 클래스를 대체할 수 있어야 한다.

부모 타입이 들어갈 자리에 자식 타입을 넣어도 프로그램이 기대한 방식대로 동작해야 한다.

```text
부모 클래스
  ↑
자식 클래스
```

단순히 문법적으로 상속이 가능하다는 뜻이 아니라, **행동적으로도 호환**되어야 한다.

예를 들어 `Car`의 `move()`는 앞으로 이동하는 기능인데, 어떤 자식 클래스가 `move()`를 멈춤 기능으로 바꿔버리면 기대한 동작을 깨뜨리게 된다.

---

# 8. ISP: 인터페이스 분리 원칙

**Interface Segregation Principle**

인터페이스는 클라이언트의 목적과 용도에 맞게 잘게 분리해야 한다.

하나의 큰 인터페이스보다 여러 개의 작은 인터페이스가 좋다.

```text
큰 인터페이스 하나
  ↓
작은 인터페이스 여러 개
```

### 장점

- 인터페이스의 역할이 명확해짐
- 사용하지 않는 기능에 의존하지 않음
- 대체 가능성이 높아짐

---

# 9. DIP: 의존관계 역전 원칙

**Dependency Inversion Principle**

구체 클래스에 의존하지 말고, 추상화에 의존해야 한다.

```text
좋은 의존
Service → Repository Interface

나쁜 의존
Service → MemoryRepository
Service → JpaRepository
```

의존한다는 것은 한 객체가 다른 객체의 기능을 필요로 해서 사용하는 관계를 의미한다.

구체 클래스는 변경될 가능성이 높기 때문에, 인터페이스처럼 상대적으로 안정적인 추상화에 의존하는 것이 좋다.

---

# 10. 다형성만으로 부족한 이유

인터페이스와 구현체를 나누어도, 구현 객체를 직접 생성하면 여전히 문제가 생긴다.

```java
public class MemberService {

    private final MemberRepository memberRepository =
            new MemoryMemberRepository();
}
```

위 코드는 `MemberRepository` 인터페이스를 사용하고 있지만, 동시에 `MemoryMemberRepository`라는 구체 클래스에도 의존한다.

### 문제점

- 구현체를 바꾸려면 `MemberService` 코드를 수정해야 함
- OCP 위반
- DIP 위반

따라서 다형성만으로는 SOLID 원칙을 완전히 지키기 어렵다.

이 문제를 해결하기 위해 Spring은 **의존성 주입(DI)**을 지원한다.

---

# 11. IoC

## IoC란?

**Inversion of Control**

IoC는 **제어의 역전**이라는 뜻이다.

객체 생성과 관리의 제어권을 개발자가 직접 가지는 것이 아니라, 프레임워크가 대신 가지는 구조를 말한다.

Spring에서는 객체를 생성하고 관리하는 역할을 **Spring Container**가 담당한다.

그래서 Spring Container를 **IoC Container**라고도 부른다.

```text
개발자 코드가 직접 객체 생성
  ↓
Spring Container가 객체 생성 및 관리
```

---

# 12. Spring Container와 Spring Bean

## 12.1 Spring Container

Spring Container는 Spring Bean을 저장하고 관리하는 공간이다.

강의에서는 이를 **스프링 빈 저장소** 또는 공용 창고처럼 설명했다.

```text
Spring Container
├── Bean A
├── Bean B
├── Bean C
└── Bean D
```

필요한 객체가 있으면 직접 생성하는 것이 아니라 컨테이너에서 꺼내 사용한다.

---

## 12.2 Spring Bean

**Spring Bean**은 Spring Container가 관리하는 객체이다.

```text
Spring Bean = 애플리케이션 전역에서 사용할 공용 객체
```

Service, Repository, Controller 같은 객체를 Bean으로 등록해두면 Spring이 생성, 관리, 주입을 담당한다.

---

# 13. 싱글톤 컨테이너

Spring Container는 기본적으로 객체를 딱 1개만 생성하고, 필요할 때마다 같은 객체를 재사용한다.

이를 **싱글톤**이라고 한다.

```text
Client A → 객체 요청 → Bean 1
Client B → 객체 요청 → Bean 1
```

매번 객체를 새로 생성하지 않고 이미 만들어진 객체를 재사용하기 때문에 메모리를 효율적으로 사용할 수 있다.

---

# 14. Spring Bean 등록 방법

Spring Bean을 등록하는 방법은 크게 두 가지이다.

```text
1. 설정 파일을 통한 수동 등록
2. 컴포넌트 스캔을 통한 자동 등록
```

---

## 14.1 수동 등록

설정 클래스를 만들고, 객체를 반환하는 메서드에 `@Bean`을 붙인다.

```java
@Configuration
public class AppConfig {

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }
}
```

### 사용 어노테이션

| 어노테이션 | 의미 |
|---|---|
| `@Configuration` | 설정 클래스임을 표시 |
| `@Bean` | 반환 객체를 Spring Bean으로 등록 |

---

## 14.2 자동 등록

자동 등록은 **컴포넌트 스캔**을 이용한다.

```text
@ComponentScan
  ↓
@Component가 붙은 클래스를 찾아 Bean으로 등록
```

`@SpringBootApplication` 안에는 `@ComponentScan`이 포함되어 있다.

따라서 Spring Boot 프로젝트에서는 보통 별도 설정 없이 컴포넌트 스캔이 동작한다.

---

# 15. 컴포넌트 관련 어노테이션

다음 어노테이션들은 Spring Bean으로 자동 등록될 수 있다.

| 어노테이션 | 주로 사용하는 계층 |
|---|---|
| `@Component` | 일반 컴포넌트 |
| `@Controller` | MVC Controller |
| `@RestController` | REST API Controller |
| `@Service` | 비즈니스 로직 계층 |
| `@Repository` | 데이터 접근 계층 |

예시:

```java
@Service
public class MemberService {
}
```

```java
@Repository
public class MemoryMemberRepository implements MemberRepository {
}
```

---

# 16. DI

## DI란?

**Dependency Injection**

DI는 **의존성 주입**이라는 뜻이다.

내가 의존하는 객체를 직접 생성하지 않고, 외부에서 주입받는 방식이다.

```text
직접 생성
MemberService → new MemoryMemberRepository()

의존성 주입
MemberService ← Spring Container가 MemberRepository 주입
```

DI는 IoC를 구현하는 대표적인 방법이다.

---

## 16.1 DI의 장점

DI를 사용하면 `MemberService`는 구체 구현체가 아니라 인터페이스에만 의존할 수 있다.

```java
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
```

### 장점

- 구현체 변경 시 Service 코드 변경 최소화
- OCP 만족
- DIP 만족
- 클래스 간 결합도 감소
- 유연성과 유지보수성 향상

---

# 17. 의존관계 자동 주입

Spring은 `@Autowired`를 통해 필요한 Bean을 자동으로 찾아 주입한다.

```java
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
```

Spring Container에 `MemberRepository` 타입의 Bean이 등록되어 있으면 자동으로 찾아서 넣어준다.

---

# 18. 의존성 주입 방법

의존성 주입 방법은 크게 세 가지가 있다.

```text
1. 생성자 주입
2. 수정자 주입
3. 필드 주입
```

---

## 18.1 생성자 주입

가장 권장되는 방식이다.

```java
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
```

### 장점

- 생성자 호출 시점에 딱 한 번만 호출됨
- 의존성 주입을 강제할 수 있음
- `final` 필드를 사용할 수 있어 불변성을 지킬 수 있음
- 테스트하기 좋음

생성자가 하나라면 `@Autowired`를 생략할 수 있다.

---

## 18.2 Lombok과 생성자 주입

Lombok의 `@RequiredArgsConstructor`를 사용하면 `final`이 붙은 필드에 대한 생성자를 자동으로 만들어준다.

```java
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
}
```

실무와 과제에서는 위 방식처럼 생성자 주입을 간단히 작성할 수 있다.

---

## 18.3 수정자 주입

Setter를 통해 의존성을 주입하는 방식이다.

```java
@Autowired
public void setMemberRepository(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
}
```

### 특징

- 변경 가능성이 있는 의존관계에 사용
- 선택적인 의존성 주입이 가능
- 객체 생성 이후에도 의존성이 바뀔 수 있음

---

## 18.4 필드 주입

필드에 바로 `@Autowired`를 붙이는 방식이다.

```java
@Autowired
private MemberRepository memberRepository;
```

하지만 권장되지 않는다.

### 권장하지 않는 이유

- 의존 관계를 파악하기 어려움
- 테스트하기 어려움
- 의존성이 변경될 가능성이 있음
- 객체 생성 시점에 필요한 의존성을 강제하기 어려움

---

# 19. 조회되는 Bean이 2개 이상일 때

같은 타입의 Bean이 여러 개 등록되어 있으면 Spring은 어떤 Bean을 주입해야 할지 판단하기 어렵다.

이때 다음 방법을 사용할 수 있다.

```text
@Qualifier
@Primary
```

---

## 19.1 @Qualifier

`@Qualifier`는 Bean에 추가 이름표를 붙여주는 방식이다.

```java
@Repository
@Qualifier("memoryRepository")
public class MemoryMemberRepository implements MemberRepository {
}
```

사용하는 쪽에서 특정 이름표를 가진 Bean을 명시할 수 있다.

```java
public MemberService(
        @Qualifier("memoryRepository") MemberRepository memberRepository
) {
    this.memberRepository = memberRepository;
}
```

---

## 19.2 @Primary

`@Primary`는 우선순위를 정하는 방식이다.

같은 타입의 Bean이 여러 개 있을 때 `@Primary`가 붙은 Bean을 먼저 선택한다.

```java
@Repository
@Primary
public class JpaMemberRepository implements MemberRepository {
}
```

---

# 20. 인터페이스는 언제 도입할까?

기능을 확장할 가능성이 명확하다면 인터페이스를 먼저 도입하는 것이 좋다.

하지만 확장 가능성이 거의 없다면 처음부터 모든 클래스에 인터페이스를 만들 필요는 없다.

강의에서는 다음 방향도 좋은 선택이라고 정리했다.

```text
우선 구체 클래스로 구현
  ↓
나중에 확장이 필요해졌을 때 인터페이스 도입
```

즉, 객체 지향 설계는 원칙을 무조건 기계적으로 적용하는 것이 아니라, 변경 가능성과 프로젝트 상황을 고려해서 적용해야 한다.

---

# 21. Service와 Repository 리팩터링 방향

온라인 쇼핑몰 프로젝트에 SOLID 원칙을 적용하려면 Controller, Service, Repository의 책임을 분리하고 의존성을 인터페이스 중심으로 정리하는 것이 좋다.

예시 구조:

```text
Controller
  ↓
Service Interface
  ↓
Service Implementation
  ↓
Repository Interface
  ↓
Repository Implementation
```

Spring Data JPA를 사용한다면 Repository는 보통 인터페이스로 작성한다.

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
}
```

Service는 Repository를 직접 생성하지 않고 생성자 주입으로 받는다.

```java
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
}
```

---

# 22. 전체 흐름 정리

이번 주차의 핵심 흐름은 다음과 같다.

```text
1. 객체 지향 프로그래밍 이해

      ↓

2. 역할과 구현 분리

Interface
Implementation

      ↓

3. SOLID 원칙 학습

SRP
OCP
LSP
ISP
DIP

      ↓

4. 다형성만으로 부족한 부분 확인

OCP 위반
DIP 위반

      ↓

5. Spring IoC Container 이해

Spring Container
Spring Bean

      ↓

6. DI로 의존성 주입

Constructor Injection
@Autowired
@RequiredArgsConstructor

      ↓

7. SOLID 원칙을 고려한 리팩터링
```

---

# 23. 과제

## 23.1 쇼핑몰 프로젝트 리팩터링

온라인 쇼핑몰 프로젝트에 SOLID 원칙을 최대한 적용하여 리팩터링한다.

고려할 점:

- Controller는 HTTP 요청과 응답 처리에 집중
- Service는 비즈니스 로직 담당
- Repository는 데이터 접근 담당
- 직접 `new`로 의존 객체를 생성하지 않기
- 생성자 주입 사용
- 필요한 경우 인터페이스를 통해 역할과 구현 분리
- 변경 가능성이 낮은 부분은 과도하게 추상화하지 않기

---

## 23.2 WIL 작성

이번 주차 학습 내용을 `wil.md`로 정리한다.

포함하면 좋은 내용:

- 객체 지향 프로그래밍이란 무엇인지
- 다형성에서 역할과 구현을 어떻게 나누는지
- SOLID 5원칙 요약
- IoC, DI, Spring Bean의 의미
- 생성자 주입을 권장하는 이유
- 프로젝트에 적용한 리팩터링 내용

---

# 24. 핵심 암기

## 객체 지향

```text
OOP = 객체들의 상호작용으로 프로그램을 구성하는 방식

추상화
캡슐화
상속
다형성
```

---

## 다형성

```text
역할 = 인터페이스
구현 = 인터페이스를 구현한 클래스

클라이언트는 구현이 아니라 역할에 의존한다.
```

---

## SOLID

```text
SRP = 하나의 클래스는 하나의 책임
OCP = 확장에는 열림, 수정에는 닫힘
LSP = 자식은 부모를 대체 가능
ISP = 인터페이스는 작고 명확하게 분리
DIP = 구체 클래스가 아니라 추상화에 의존
```

---

## Spring

```text
IoC = 객체 생성과 관리의 제어권을 Spring이 가짐
DI = 필요한 의존 객체를 외부에서 주입받음
Spring Bean = Spring Container가 관리하는 객체
Spring Container = Bean을 생성하고 관리하는 공간
```

---

## 의존성 주입

```text
생성자 주입 = 가장 권장
수정자 주입 = 변경 가능하거나 선택적인 의존성
필드 주입 = 권장하지 않음

@Autowired = 필요한 Bean 자동 주입
@RequiredArgsConstructor = final 필드 생성자 자동 생성
@Qualifier = 특정 Bean 선택
@Primary = 우선순위 Bean 지정
```

---

# 25. 한 줄 요약

> **Spring은 IoC Container와 DI를 통해 객체 생성과 의존관계 관리를 대신 수행하고, 이를 통해 SOLID 원칙을 지키기 쉬운 유연하고 확장 가능한 객체 지향 프로그램을 만들 수 있게 해준다.**
