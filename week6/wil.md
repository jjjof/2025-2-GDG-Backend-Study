# 백엔드 정규 스터디 6주차

> **주제:** 예외 처리와 Swagger  
> **내용:** DTO 유효성 검사 -> 전역 예외 처리 -> 커스텀 예외 -> 에러 메시지 상수화 -> Swagger API 문서화

---

# 1. 5주차 복습

지난 주차에서는 객체 지향 설계와 Spring Bean, DI, IoC를 학습했다.

## 1.1 객체 지향의 네 가지 특징

| 특징 | 의미 |
|---|---|
| 추상화 | 객체의 공통 속성과 기능을 추출하여 정의 |
| 캡슐화 | 연관 있는 속성과 기능을 하나로 묶고 외부로부터 보호 |
| 상속 | 기존 클래스의 속성과 기능을 새로운 클래스가 재사용 |
| 다형성 | 같은 역할을 여러 구현 방식으로 대체 가능 |

---

## 1.2 SOLID 원칙

**SOLID**는 좋은 객체 지향 설계를 위한 5가지 원칙이다.

| 원칙 | 이름 | 의미 |
|---|---|---|
| SRP | 단일 책임 원칙 | 하나의 클래스는 하나의 책임만 가져야 한다 |
| OCP | 개방-폐쇄 원칙 | 확장에는 열려 있고 수정에는 닫혀 있어야 한다 |
| LSP | 리스코프 치환 원칙 | 하위 타입은 상위 타입을 대체할 수 있어야 한다 |
| ISP | 인터페이스 분리 원칙 | 인터페이스는 목적에 맞게 작게 분리해야 한다 |
| DIP | 의존관계 역전 원칙 | 구체 클래스보다 추상화에 의존해야 한다 |

---

## 1.3 IoC, DI, Spring Bean

**IoC(Inversion of Control)**는 객체 생성과 관리의 제어권을 개발자가 아니라 Spring Container가 가지는 구조이다.

**DI(Dependency Injection)**는 필요한 의존 객체를 직접 생성하지 않고 외부에서 주입받는 방식이다.

```text
직접 생성
MemberService -> new JpaMemberRepository()

의존성 주입
MemberService <- Spring Container가 MemberRepository Bean 주입
```

**Spring Bean**은 Spring Container가 생성하고 관리하는 객체이다.

---

# 2. 현재 API의 문제점

기존 API는 클라이언트가 항상 올바른 데이터를 보낸다고 가정했다.

예를 들어 회원 등록 API에 다음과 같은 잘못된 요청이 들어올 수 있다.

```text
password를 100자 넘게 보냄
전화번호 형식이 맞지 않음
필수값을 보내지 않음
```

이런 요청은 클라이언트가 잘못 보낸 요청이므로 서버 오류인 `500 Internal Server Error`로 처리하면 의미가 맞지 않다.

```text
500 Internal Server Error
= 서버 내부 오류
= 서버가 잘못했거나 예상하지 못한 문제가 발생했다는 의미
```

클라이언트 요청 데이터가 잘못된 경우에는 보통 `400 Bad Request`를 응답하는 것이 적절하다.

---

# 3. HTTP 상태 코드 복습

| 상태 코드 | 의미 |
|---|---|
| 200 OK | 요청이 성공적으로 처리됨 |
| 201 Created | 요청이 성공하여 새로운 리소스가 생성됨 |
| 400 Bad Request | 클라이언트 요청이 잘못됨 |
| 404 Not Found | 요청한 리소스를 찾을 수 없음 |
| 500 Internal Server Error | 서버 내부 오류로 요청을 처리할 수 없음 |

핵심은 클라이언트의 잘못과 서버의 잘못을 상태 코드로 구분하는 것이다.

```text
클라이언트 입력 문제 -> 4xx
서버 내부 문제 -> 5xx
```

---

# 4. 유효성 검사

## 4.1 유효성 검사란?

유효성 검사는 요청으로 들어오는 데이터가 올바른 형식인지 검사하는 것이다.

Spring에서는 보통 **DTO에 검증 어노테이션을 붙이고**, Controller에서 `@Valid`를 사용하여 검증을 실행한다.

```text
Request Body
   ↓
DTO 변환
   ↓
@Valid로 제약 조건 검사
   ↓
조건 위반 시 400 Bad Request
```

주의할 점은 유효성 검사가 **입력 데이터의 형식**을 검사한다는 것이다.

```text
검사 가능
- null 여부
- 문자열 길이
- 전화번호 패턴
- 숫자 최소/최대값

검사 불가능
- 실제 DB에 존재하는 회원인지
- 이미 가입된 loginId인지
```

DB 조회가 필요한 비즈니스 판단은 Service 계층에서 처리해야 한다.

---

## 4.2 의존성 추가

유효성 검사를 사용하려면 `build.gradle`에 validation 의존성을 추가한다.

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-validation'
}
```

의존성 추가 후 Gradle을 다시 로드해야 한다.

---

## 4.3 DTO 검증 어노테이션

DTO 필드에 제약 조건과 에러 메시지를 명시한다.

```java
@Getter
public class MemberCreateRequest {

    @NotNull(message = "로그인 아이디는 필수입니다.")
    @Size(min = 4, max = 20, message = "로그인 아이디는 4자 이상 20자 이하입니다.")
    private String loginId;

    @NotNull(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하입니다.")
    private String password;

    @NotNull(message = "전화번호는 필수입니다.")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식은 010-xxxx-xxxx 입니다.")
    private String phoneNumber;
}
```

자주 사용하는 검증 어노테이션은 다음과 같다.

| 어노테이션 | 의미 |
|---|---|
| `@NotNull` | null이면 안 됨 |
| `@NotBlank` | null, 빈 문자열, 공백 문자열이면 안 됨 |
| `@Size` | 문자열 또는 컬렉션 크기 제한 |
| `@Pattern` | 정규식 패턴 검사 |
| `@Min` | 최소값 검사 |
| `@Max` | 최대값 검사 |

---

## 4.4 Controller에서 @Valid 사용

DTO에 제약 조건을 붙였더라도 Controller에서 `@Valid`를 붙여야 실제 검사가 실행된다.

```java
@PostMapping
public ResponseEntity<Void> createMember(
        @RequestBody @Valid MemberCreateRequest request
) {
    Long memberId = memberService.createMember(request);
    return ResponseEntity.created(URI.create("/members/" + memberId)).build();
}
```

`@Valid` 검증에 실패하면 `MethodArgumentNotValidException`이 발생한다.

---

# 5. 리팩터링: updateInfo 책임 분리

기존 `Member` 엔티티의 `updateInfo`가 다음 두 가지 책임을 함께 가지고 있었다.

```text
1. 도메인 모델 책임
   - 실제 값을 변경

2. 비즈니스 로직 책임
   - 어떤 필드를 업데이트할지 null 체크로 판단
```

이는 하나의 메서드가 여러 책임을 가지는 구조이므로 SRP를 위반할 수 있다.

리팩터링 방향은 다음과 같다.

```text
Service
-> 어떤 값으로 수정할지 판단

Entity
-> 전달받은 값으로 자신의 상태 변경
```

예시:

```java
String password = request.getPassword() != null
        ? request.getPassword()
        : member.getPassword();

String phoneNumber = request.getPhoneNumber() != null
        ? request.getPhoneNumber()
        : member.getPhoneNumber();

String address = request.getAddress() != null
        ? request.getAddress()
        : member.getAddress();

member.updateInfo(password, phoneNumber, address);
```

Entity는 null 판단을 하지 않고, Service가 최종 수정 값을 결정한다.

---

# 6. 예외 처리

## 6.1 왜 예외 처리가 필요한가?

유효성 검사를 적용하면 잘못된 요청은 막을 수 있다. 하지만 클라이언트 입장에서는 단순히 `400 Bad Request`만 보면 어떤 필드가 왜 잘못되었는지 알기 어렵다.

따라서 에러가 발생했을 때 원인을 알려주는 응답 객체를 직접 만들어 내려줄 필요가 있다.

예시:

```json
{
  "message": "비밀번호는 8자 이상 20자 이하입니다."
}
```

---

## 6.2 ErrorResponse

에러 정보를 반환하기 위한 DTO를 만든다.

```java
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String message;
}
```

현재는 `message`만 담지만, 필요하면 나중에 `code`, `status`, `timestamp` 같은 필드를 추가할 수 있다.

---

# 7. Global Exception Handler

## 7.1 전역 예외 핸들러란?

`GlobalExceptionHandler`는 애플리케이션 전역에서 발생하는 예외를 한 곳에서 처리하는 클래스이다.

컨트롤러마다 `try-catch`를 반복하지 않고, 예외 종류에 따라 공통 응답을 만들 수 있다.

```text
Controller 또는 Service에서 예외 발생
   ↓
Spring MVC 예외 처리 흐름
   ↓
GlobalExceptionHandler의 @ExceptionHandler 메서드 실행
   ↓
ErrorResponse 반환
```

---

## 7.2 @ControllerAdvice

`@ControllerAdvice`는 여러 Controller에서 발생하는 예외를 중앙에서 처리할 수 있게 해준다.

```java
@ControllerAdvice
public class GlobalExceptionHandler {
}
```

즉, 모든 컨트롤러의 공통 관심사인 에러 처리를 별도 클래스로 분리하는 역할을 한다.

---

## 7.3 @ExceptionHandler

`@ExceptionHandler`는 어떤 예외가 발생했을 때 어떤 메서드가 처리할지 연결한다.

```java
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleUnknownException(Exception e) {
    ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
    return ResponseEntity.internalServerError().body(errorResponse);
}
```

의미는 다음과 같다.

```text
Exception 타입 예외 발생
-> handleUnknownException 메서드 실행
-> 500 Internal Server Error 응답 생성
```

`Exception.class`는 대부분의 일반 예외의 부모 타입이므로, 구체적인 핸들러에서 처리하지 못한 예외를 마지막에 처리하는 용도로 사용할 수 있다.

---

## 7.4 유효성 검사 실패 처리

`@Valid` 검증 실패 시 `MethodArgumentNotValidException`이 발생한다.

이를 전역 핸들러에서 잡아 첫 번째 검증 실패 메시지를 응답할 수 있다.

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex
) {
    String message = ex.getBindingResult()
            .getAllErrors()
            .get(0)
            .getDefaultMessage();

    ErrorResponse errorResponse = new ErrorResponse(message);
    return ResponseEntity.badRequest().body(errorResponse);
}
```

결과 예시:

```json
{
  "message": "비밀번호는 8자 이상 20자 이하입니다."
}
```

---

# 8. AOP 관점에서 보는 예외 처리

**AOP(Aspect-Oriented Programming)**는 관점 지향 프로그래밍이다.

객체 지향 프로그래밍이 핵심 기능을 클래스와 메서드로 모듈화한다면, AOP는 여러 클래스에 반복되는 공통 기능을 분리한다.

```text
OOP
-> 회원 관리, 주문 처리, 상품 관리 같은 핵심 기능을 모듈화

AOP
-> 로깅, 트랜잭션, 보안, 예외 처리 같은 공통 관심사를 모듈화
```

관련 용어는 다음과 같다.

| 용어 | 의미 |
|---|---|
| Aspect | 여러 클래스에 걸친 공통 관심사를 모듈화한 것 |
| Join Point | Aspect가 적용될 수 있는 지점 |
| Advice | 특정 Join Point에서 실행되는 동작 |

`GlobalExceptionHandler`는 모든 Controller의 에러 처리라는 공통 관심사를 별도 클래스로 분리한다는 점에서 AOP 관점으로 이해할 수 있다.

다만 Spring MVC 내부 동작 기준으로는 `@ControllerAdvice`와 `@ExceptionHandler`를 이용한 예외 처리 체인에 가깝다.

---

# 9. 커스텀 예외 처리

## 9.1 커스텀 예외가 필요한 이유

모든 예외를 `RuntimeException`으로 던지면 어떤 상황에서 발생한 예외인지 구분하기 어렵다.

예를 들어 다음 두 상황은 서로 다른 HTTP 상태 코드가 더 적절하다.

```text
잘못된 요청 값 -> 400 Bad Request
존재하지 않는 리소스 -> 404 Not Found
```

따라서 상황별 커스텀 예외 클래스를 만들고, 전역 예외 핸들러에서 각각 처리한다.

---

## 9.2 RuntimeException 상속

실행 중 발생하는 비즈니스 예외는 보통 `RuntimeException`을 상속하여 만든다.

```java
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
```

```java
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
```

---

## 9.3 커스텀 예외 핸들러

```java
@ExceptionHandler(BadRequestException.class)
public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
    ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
    return ResponseEntity.badRequest().body(errorResponse);
}
```

```java
@ExceptionHandler(NotFoundException.class)
public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
    ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
}
```

정리하면 다음과 같다.

```text
BadRequestException -> 400 Bad Request
NotFoundException -> 404 Not Found
Exception -> 500 Internal Server Error
```

---

# 10. 에러 메시지 클래스

## 10.1 왜 필요한가?

예외 메시지 문자열을 여러 곳에서 직접 작성하면 같은 문구가 중복되고, 나중에 수정하기 어렵다.

```java
throw new NotFoundException("회원을 찾을 수 없습니다.");
```

같은 메시지를 여러 파일에서 반복하면 메시지 수정 시 모든 파일을 찾아 수정해야 한다.

따라서 에러 메시지를 상수로 모아 관리한다.

---

## 10.2 ErrorMessage

```java
public class ErrorMessage {

    public static final String MEMBER_NOT_FOUND = "회원을 찾을 수 없습니다.";
    public static final String MEMBER_ALREADY_EXISTS = "이미 존재하는 회원입니다.";

    public static final String LOGIN_ID_NOT_NULL = "로그인 아이디는 필수입니다.";
    public static final String LOGIN_ID_SIZE = "로그인 아이디는 4자 이상 20자 이하입니다.";
    public static final String PASSWORD_NOT_NULL = "비밀번호는 필수입니다.";
    public static final String PASSWORD_SIZE = "비밀번호는 8자 이상 20자 이하입니다.";
    public static final String PHONE_NUMBER_NOT_NULL = "전화번호는 필수입니다.";
    public static final String PHONE_NUMBER_PATTERN = "전화번호 형식은 010-xxxx-xxxx 입니다.";
    public static final String ADDRESS_NOT_NULL = "주소는 필수입니다.";
    public static final String ADDRESS_SIZE = "주소는 1자 이상 255자 이하입니다.";
}
```

사용 예시:

```java
throw new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND);
```

DTO에도 같은 방식으로 적용할 수 있다.

```java
@NotNull(message = LOGIN_ID_NOT_NULL)
@Size(min = 4, max = 20, message = LOGIN_ID_SIZE)
private String loginId;
```

---

# 11. API 문서화

## 11.1 API 문서화란?

API 문서화는 백엔드 API 명세를 문서로 정리하여 공유하는 것이다.

프론트엔드와 협업할 때 API 문서가 있으면 다음 내용을 쉽게 확인할 수 있다.

- 요청 URL
- HTTP Method
- Request Body
- Response Body
- 상태 코드
- 에러 응답

---

## 11.2 Swagger와 OpenAPI

이번 주차에서는 API 문서화 도구로 Swagger를 사용한다.

```text
OpenAPI
-> API 표준 명세

Swagger UI
-> OpenAPI 명세를 보기 좋은 웹 화면으로 보여주는 도구

springdoc
-> Spring 프로젝트에서 OpenAPI 문서를 자동 생성해주는 라이브러리
```

---

## 11.3 Swagger 의존성 추가

`build.gradle`에 springdoc 의존성을 추가한다.

```gradle
dependencies {
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.14'
}
```

Spring 애플리케이션 실행 후 다음 주소에서 Swagger UI를 확인할 수 있다.

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger UI에서는 API 목록을 확인할 수 있고, 직접 요청을 보내 테스트할 수도 있다.

---

## 11.4 Swagger 문서화 어노테이션

Swagger 문서를 더 읽기 좋게 만들기 위해 다음 어노테이션을 사용할 수 있다.

| 어노테이션 | 위치 | 역할 |
|---|---|---|
| `@Tag` | Controller 클래스 | API 그룹화 |
| `@Operation` | Controller 메서드 | API 요약과 설명 작성 |
| `@ApiResponse` | Controller 메서드 | 응답 상태 코드 설명 |

예시:

```java
@Tag(name = "Member", description = "회원 API")
@RestController
@RequestMapping("/members")
public class MemberController {

    @Operation(summary = "회원 생성", description = "새로운 회원을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "회원 생성 성공")
    @PostMapping
    public ResponseEntity<Void> createMember(
            @RequestBody @Valid MemberCreateRequest request
    ) {
        Long memberId = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + memberId)).build();
    }
}
```

---

# 12. 전체 흐름 정리

이번 주차의 핵심 흐름은 다음과 같다.

```text
1. 잘못된 요청 데이터 문제 확인

      ↓

2. DTO 유효성 검사 추가

@NotNull
@Size
@Pattern
@Valid

      ↓

3. 유효성 실패 시 400 응답 처리

MethodArgumentNotValidException

      ↓

4. 전역 예외 처리 구조 도입

@ControllerAdvice
@ExceptionHandler
ErrorResponse

      ↓

5. 커스텀 예외로 상황별 상태 코드 구분

BadRequestException -> 400
NotFoundException -> 404
Exception -> 500

      ↓

6. 에러 메시지 상수화

ErrorMessage

      ↓

7. Swagger로 API 문서화

OpenAPI
Swagger UI
springdoc
```

---

# 13. 과제

## 13.1 유효성 검증 추가

DTO에 유효성 검증 어노테이션을 추가한다.

```text
@NotNull
@Size
@Pattern
@NotBlank
@Min
@Max
```

Controller 메서드의 Request DTO에는 `@Valid`를 붙인다.

---

## 13.2 Global Exception Handler 생성

전역 예외 핸들러를 만들고 Postman으로 테스트한다.

필수 확인:

```text
4xx 상태 코드
+
에러 메시지 응답
```

member 도메인을 제외하고 `order` 또는 `product` 도메인 관련 엔드포인트 1개를 선택하여 실패 케이스를 테스트한다.

---

## 13.3 Swagger UI 확인

Swagger 의존성을 추가하고 다음 주소에서 API 문서를 확인한다.

```text
http://localhost:8080/swagger-ui/index.html
```

`week6/wil.md`에 다음 스크린샷을 추가한다.

```text
1. Postman 실패 응답 스크린샷
2. Swagger UI 확인 스크린샷
```

---

# 14. 쇼핑몰 어드민 기능 명세

이번 과제는 쇼핑몰 어드민을 기준으로 한다.

## Member

- 회원 등록
- 회원 조회
- 회원 수정
- 회원 삭제

## Product

- 상품 등록
- 상품 조회
- 상품 수정
- 상품 삭제

## Order

- 상품 주문
- 주문 내역 조회
- 주문 취소

언급되지 않은 세부 명세는 자유롭게 정할 수 있다.

---

# 15. 핵심 암기

## 유효성 검사

```text
DTO에 검증 어노테이션 작성
Controller에서 @Valid로 검사 실행

형식 검증은 DTO
DB 조회가 필요한 판단은 Service
```

---

## 예외 처리

```text
@ControllerAdvice
-> 모든 Controller의 예외를 중앙에서 처리

@ExceptionHandler
-> 특정 예외 타입과 처리 메서드를 연결

ErrorResponse
-> 에러 응답 body DTO
```

---

## 상태 코드

```text
400 Bad Request
-> 클라이언트 요청 값이 잘못됨

404 Not Found
-> 요청한 리소스를 찾을 수 없음

500 Internal Server Error
-> 서버 내부 오류
```

---

## 커스텀 예외

```text
BadRequestException
-> 잘못된 요청

NotFoundException
-> 리소스 없음

Exception
-> 예상하지 못한 서버 오류
```

---

## Swagger

```text
OpenAPI = API 표준 명세
Swagger UI = API 문서를 웹 화면으로 제공
springdoc = Spring에서 OpenAPI 문서 자동 생성

접속 주소:
http://localhost:8080/swagger-ui/index.html
```

---

# 16. 한 줄 요약

> **6주차는 잘못된 요청을 DTO 유효성 검사로 막고, 전역 예외 처리로 일관된 에러 응답을 만들며, Swagger로 API 명세를 자동 문서화하는 방법을 학습했다.**

---

# 17. 추가 질문 정리

## 17.1 GlobalExceptionHandler를 왜 쓰는가?

`GlobalExceptionHandler`의 핵심 목적은 단순히 "모든 에러 메시지를 처리한다"가 아니다.

핵심은 **예외가 HTTP 응답으로 바뀌는 규칙을 한곳에 모아두는 것**이다.

만약 전역 예외 처리를 사용하지 않으면 Controller마다 다음과 같은 코드를 반복해야 한다.

```java
try {
    Member member = memberService.getMemberById(memberId);
    return ResponseEntity.ok(member);
} catch (RuntimeException e) {
    return ResponseEntity
            .status(404)
            .body(new ErrorResponse(e.getMessage()));
}
```

이런 코드가 모든 Controller에 반복되면 다음 문제가 생긴다.

- 정상 흐름과 실패 흐름이 Controller 안에 섞임
- 같은 에러 응답 코드를 여러 번 작성해야 함
- API마다 에러 응답 형식이 달라질 수 있음
- 상태 코드 정책을 바꾸려면 여러 파일을 수정해야 함

`GlobalExceptionHandler`를 사용하면 Controller는 정상 응답만 담당하고, 실패 응답은 전역 예외 처리기가 담당한다.

```text
Controller
-> 성공 응답 처리

Service
-> 비즈니스 로직 수행, 실패 시 예외 발생

GlobalExceptionHandler
-> 예외를 HTTP 에러 응답으로 변환
```

즉, 전역 예외 처리의 존재 이유는 **API 전체의 실패 응답 정책을 통일하기 위해서**이다.

---

## 17.2 설정하지 않은 예외는 어디로 가는가?

현재 코드에 다음 핸들러가 있다.

```java
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleUnknownException(Exception e) {
    ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
    return ResponseEntity.internalServerError().body(errorResponse);
}
```

`Exception.class`는 대부분의 일반 예외의 부모 타입이다.

따라서 구체적으로 등록하지 않은 예외라도 `Exception`의 하위 타입이면 이 메서드로 처리될 수 있다.

예:

```text
RuntimeException
NullPointerException
IllegalArgumentException
MethodArgumentNotValidException
HttpMessageNotReadableException
```

단, 더 구체적인 핸들러가 있으면 그 핸들러가 먼저 선택된다.

```text
MethodArgumentNotValidException 발생
-> MethodArgumentNotValidException 핸들러가 있으면 그쪽으로 감
-> 없으면 Exception 핸들러로 감
```

하지만 모든 문제가 `Exception.class`로 잡히는 것은 아니다.

자바의 실패 객체 구조는 다음과 같다.

```text
Throwable
├── Exception
│   ├── RuntimeException
│   ├── IOException
│   └── ...
└── Error
    ├── OutOfMemoryError
    ├── StackOverflowError
    └── ...
```

현재 핸들러는 `Exception.class`를 처리하므로 `Error` 계열은 보통 처리 대상이 아니다.

또한 `@ControllerAdvice`는 Spring MVC 요청 처리 흐름에서 발생한 예외를 처리한다.

따라서 다음 경우는 전역 예외 핸들러 대상이 아닐 수 있다.

- 애플리케이션 시작 중 발생한 예외
- 스케줄러나 별도 스레드에서 발생한 예외
- JVM 자체가 종료될 정도의 심각한 오류
- `Error` 계열의 JVM 수준 문제

정리:

```text
Controller 요청 처리 중 발생한 Exception 계열
-> GlobalExceptionHandler가 처리 가능

Error 계열
-> 보통 처리 대상 아님

Spring MVC 요청 흐름 밖에서 발생한 예외
-> @ControllerAdvice 대상 아님
```

---

## 17.3 자바는 모든 실패를 객체로 만들 수 있는가?

자바는 실행 중 발생하는 많은 실패를 `Throwable` 객체로 표현한다.

대표 구조는 다음과 같다.

```text
Object
└── Throwable
    ├── Exception
    └── Error
```

예를 들어 직접 예외를 던지면 예외 객체가 만들어진다.

```java
throw new RuntimeException("회원을 찾을 수 없습니다.");
```

이때 만들어진 예외 객체 안에는 다음 정보가 들어갈 수 있다.

- 예외 메시지
- 실제 예외 타입
- 스택트레이스
- 원인 예외

```java
e.getMessage();
e.getClass();
e.getStackTrace();
e.getCause();
```

하지만 하드웨어적, 운영체제적, JVM 외부의 모든 실패를 자바 객체로 만들 수 있는 것은 아니다.

예를 들어 다음 상황은 자바 코드가 실행될 기회 자체가 없을 수 있다.

```text
컴퓨터 전원이 꺼짐
운영체제가 프로세스를 강제 종료함
JVM 자체가 크래시남
메모리 손상이 너무 심해 JVM이 복구 불가능함
```

따라서 정확히는 다음과 같이 이해하면 된다.

```text
JVM이 감지하고 프로그램 흐름 안에서 전달할 수 있는 실패
-> Throwable 객체로 표현 가능

JVM이 계속 실행할 수 없거나 코드 실행 기회가 없는 실패
-> 자바 객체로 처리 불가능
```

즉, 자바는 많은 실행 실패를 객체로 표현하지만, 모든 소프트웨어적/하드웨어적 실패를 객체로 만들 수 있는 것은 아니다.
