# 🖥️ Backoffice 관리 시스템

<p align="center">
   <img width="1500" height="1500" alt="Image" src="https://github.com/user-attachments/assets/dfd56ea3-885e-4205-a466-0665cdf77552" />
</p>

---

## 🚩 프로젝트 개요
- **프로젝트명:** Backoffice 관리 시스템
- **개발자:** 김민섭
- **DB 이름:** `Backoffice_db`
- **DB 포트:** 3306
- **설명:**  
관리자 전용 웹 시스템으로, 회원, 상품, 주문, 리뷰, 통계 등 전사 운영 데이터를 관리합니다.  
권한 기반 접근 제어(SUPER_ADMIN, ADMIN)로 안전하게 관리할 수 있습니다.

---

## 💨 개발 기간
- 2025.07.22 ~ 2025.08.19

---

## 🛠 개발 환경
- **운영체제:** Windows 10/11
- **IDE:** IntelliJ IDEA
- **JDK:** 21
- **데이터베이스:** MySQL
- **빌드 툴:** Gradle
- **버전 관리:** GitHub

---

## ✨ Dependencies
- Spring Boot DevTools
- Lombok
- Spring Data JPA
- MariaDB Driver
- Spring Security
- Spring Web
- Thymeleaf
- Validation

---

## 💻 기술 스택

### Backend
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">

### Frontend
<img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white" alt="HTML5">
<img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white" alt="Thymeleaf">
<img src="https://img.shields.io/badge/Tailwind CSS-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white">
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white">

### Databases
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">

---

## 🗃️ ER 다이어그램

![ER Diagram](src/main/resources/static/images/functionImage/ARTAUCTION-ER-DIAGRAM.png)

## 📑 사용자 요구사항 명세서 [Notion](https://www.notion.so/aa74ac5296904e0d8b3bd8b5961408c6?v=bea59306a5964c9fbf44a8220f6476aa&pvs=4 "사용자 요구사항 명세서")

![사용자 요구사항 명세서](src/main/resources/static/images/functionImage/ARTAUCTION-요구사항정의서.png)



## 👤 주요 기능

### 관리자 기능
| 요구사항 코드 | 기능 | 권한 |
|---------------|------|------|
| REQ-ADM-001 | 관리자 로그인 | SUPER_ADMIN, ADMIN |
| REQ-ADM-002 | 관리자 생성 | SUPER_ADMIN |
| REQ-ADM-003 | 비밀번호 변경 | ADMIN |
| REQ-ADM-004 | 권한 변경 | SUPER_ADMIN |

### 회원 관리
| 요구사항 코드 | 기능 | 권한 |
|---------------|------|------|
| REQ-USR-001 | 회원 목록 조회 | ADMIN,SUPER_ADMIN |
| REQ-USR-002 | 회원 상세 조회 | ADMIN,SUPER_ADMIN |
| REQ-USR-003 | 회원 상태 변경 | ADMIN,SUPER_ADMIN |
| REQ-USR-004 | 회원 삭제 | ADMIN,SUPER_ADMIN |

### 상품 관리
| 요구사항 코드 | 기능 | 권한 |
|---------------|------|------|
| REQ-PROD-001 | 상품 목록 조회 | ADMIN |
| REQ-PROD-002 | 상품 등록 | ADMIN |
| REQ-PROD-003 | 상품 수정 | ADMIN |
| REQ-PROD-004 | 상품 삭제 | ADMIN |
| REQ-PROD-005 | 카테고리 관리 | ADMIN |

### 주문 관리
| 요구사항 코드 | 기능 | 권한 |
|---------------|------|------|
| REQ-ORD-001 | 주문 목록 조회 | ADMIN |
| REQ-ORD-002 | 주문 상세 보기 | ADMIN |
| REQ-ORD-003 | 주문 상태 변경 | ADMIN |

### 리뷰 관리
| 요구사항 코드 | 기능 | 권한 |
|---------------|------|------|
| REQ-REV-001 | 리뷰 목록 조회 | ADMIN |
| REQ-REV-002 | 리뷰 상세 조회 | ADMIN |
| REQ-REV-003 | 리뷰 삭제 | ADMIN |
| REQ-REV-004 | 신고 리뷰 필터링 | ADMIN |
| REQ-REV-005 | 구매자 권한 인증 | USER |

### 대시보드 / 통계
| 요구사항 코드 | 기능 | 권한 |
|---------------|------|------|
| REQ-DASH-001 | 가입자 통계 조회 | ADMIN |
| REQ-DASH-002 | 매출 통계 조회 | ADMIN |
| REQ-DASH-003 | 최근 활동 내역 | ADMIN |

---

## ⚙️ 프로젝트 구조

```admin-backoffice/
├─ build.gradle
├─ settings.gradle
├─ gradlew
├─ gradlew.bat
├─ HELP.md
├─ src/
│ ├─ main/
│ │ ├─ java/
│ │ │ └─ com.minseop.admin_backoffice/
│ │ │ ├─ config/
│ │ │ │ ├─ SecurityConfig.java
│ │ │ │ └─ WebConfig.java
│ │ │ ├─ controller/
│ │ │ │ ├─ AuditLogController.java
│ │ │ │ ├─ BannedUserController.java
│ │ │ │ ├─ CartController.java
│ │ │ │ ├─ MainController.java
│ │ │ │ ├─ OrderController.java
│ │ │ │ ├─ PaymentController.java
│ │ │ │ ├─ ProductCategoryController.java
│ │ │ │ ├─ ProductController.java
│ │ │ │ ├─ ReviewController.java
│ │ │ │ ├─ UserAdminController.java
│ │ │ │ ├─ UserController.java
│ │ │ │ └─ UserProductController.java
│ │ │ ├─ domain/
│ │ │ │ ├─ AuditLog.java
│ │ │ │ ├─ Cart.java
│ │ │ │ ├─ CartItem.java
│ │ │ │ ├─ Order.java
│ │ │ │ ├─ OrderItem.java
│ │ │ │ ├─ Product.java
│ │ │ │ ├─ ProductCategory.java
│ │ │ │ ├─ Review.java
│ │ │ │ ├─ UserEntity.java
│ │ │ │ ├─ UserRole.java
│ │ │ │ └─ UserStatus.java
│ │ │ ├─ dto/
│ │ │ │ ├─ OrderForm.java
│ │ │ │ ├─ UserSignupForm.java
│ │ │ │ └─ UserUpdateForm.java
│ │ │ ├─ repository/
│ │ │ │ ├─ AuditLogRepository.java
│ │ │ │ ├─ CartItemRepository.java
│ │ │ │ ├─ CartRepository.java
│ │ │ │ ├─ OrderRepository.java
│ │ │ │ ├─ ProductCategoryRepository.java
│ │ │ │ ├─ ProductRepository.java
│ │ │ │ ├─ ReviewRepository.java
│ │ │ │ └─ UserRepository.java
│ │ │ ├─ security/
│ │ │ │ ├─ CustomAccessDeniedHandler.java
│ │ │ │ ├─ CustomAuthenticationFailureHandler.java
│ │ │ │ ├─ CustomAuthenticationSuccessHandler.java
│ │ │ │ ├─ CustomUserDetails.java
│ │ │ │ └─ CustomUserDetailsService.java
│ │ │ └─ service/
│ │ │ ├─ AuditLogService.java
│ │ │ ├─ CartService.java
│ │ │ ├─ FileStorageService.java
│ │ │ ├─ OrderService.java
│ │ │ ├─ ProductCategoryService.java
│ │ │ ├─ ProductService.java
│ │ │ ├─ ReviewService.java
│ │ │ └─ UserService.java
│ │ └─ resources/
│ │ ├─ application.yml
│ │ ├─ templates/
│ │ │ ├─ admin/
│ │ │ │ ├─ dashboard.html
│ │ │ │ ├─ user_list.html
│ │ │ │ ├─ user_detail.html
│ │ │ │ └─ product/
│ │ │ │ ├─ list.html
│ │ │ │ ├─ form.html
│ │ │ │ └─ category-list.html
│ │ │ ├─ layout/
│ │ │ │ ├─ header.html
│ │ │ │ ├─ footer.html
│ │ │ │ ├─ sidebar.html
│ │ │ │ └─ layout.html
│ │ │ └─ ... 기타 템플릿
│ │ └─ static/
│ │ ├─ js/
│ │ └─ images/
└─ README.md
```


## 🔥 개발 후기 
권한 기반 관리자 시스템, 상품/주문/회원/리뷰 관리, 통계 대시보드 등 핵심 기능을 모두 구현하였습니다.  
Spring Boot + JPA + Security + Thymeleaf 기반으로 안정적인 운영 관리 환경을 구축한 경험이 가장 큰 성과입니다.
