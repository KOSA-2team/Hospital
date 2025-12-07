# Hospital ERP System

**Spring Boot 기반의 의료진 전용 환자 관리 CRUD 프로젝트(Hospital Management System)**


---

## Tech Stack

<img width="680" alt="기술스택" src="https://github.com/user-attachments/assets/5517ebe0-e29b-4ad7-b422-ea88ef669291" />

### Backend
- **Java 17**: LTS 버전의 안정적인 언어 환경
- **Spring Boot 3.4.12**: 애플리케이션 프레임워크
- **Spring JDBC (JdbcTemplate)**: SQL 중심의 명시적인 데이터 제어
- **Gradle**: 빌드 및 의존성 관리

### Database
- **MySQL 8.0**: 관계형 데이터베이스 관리 시스템 (RDBMS)

---

## Team Roles

| 이름 | 역할 | 담당 업무 및 기여 |
|------|------|------------------|
| 이대승 | Team Leader | • 의료진 관리 (계정 생성, 권한 관리, 정보 수정)<br>• 인증/인가 시스템 (로그인, 세션, Interceptor) 구현<br>• 초기 데이터베이스 스키마 및 더미 데이터 설계 |
| 백민근 | Backend Dev | • 환자 예약 시스템 (등록, 조회, 취소, 중복 방지)<br>• 전체 시스템 아키텍처 설계 및 공통 모듈 관리<br>• 예약 상태에 따른 비즈니스 로직 구현 |
| 조승훈 | Backend Dev | • 메인 대시보드 (통계 데이터 시각화 및 일정 조회)<br>• 환자 관리 시스템 (CRUD, 검색, 이력 관리)<br>• Frontend-Backend 데이터 바인딩 최적화 |
| 이서현 | Backend Dev | • 진료 기록(EMR) 및 처방전 관리 기능 개발<br>• 진료-처방 간 트랜잭션 처리 구현<br>• 프로젝트 기능 명세서 작성 및 문서화 관리 |

---

## ERD & Database Design

<img width="550" alt="ERD" src="https://github.com/user-attachments/assets/345b7705-a284-4706-9b36-718204f6c86a" />


1. **Patient ↔ Reservation (1:N)**
   - **관계 정의**: 한 명의 환자는 병원에 여러 번 방문하여 다수의 예약 기록을 가질 수 있습니다.
   - **구현**: `reservation` 테이블이 `patient_num`을 FK로 참조합니다.

2. **MedicalStaff ↔ Reservation (1:N)**
   - **관계 정의**: 한 명의 의료진(의사)은 다수의 환자 예약을 담당합니다.
   - **구현**: `reservation` 테이블이 `medical_num`을 FK로 참조합니다.

3. **Reservation ↔ Treatment (1:1)**
   - **관계 정의**: 확정된 하나의 예약에 대해서는 단 하나의 진료 기록만 생성됩니다.
   - **구현**: `treatment` 테이블은 `reservation_num`을 PK이자 FK로 사용하여 예약과 1:1 식별 관계를 형성합니다.

4. **Treatment ↔ Prescription (1:N)**
   - **관계 정의**: 한 번의 진료 행위에서 여러 종류의 약물이 처방될 수 있습니다.
   - **구현**: 데이터 유연성을 위해 처방 정보를 별도의 `prescription` 테이블로 분리하고 `treatment_num`을 참조하여 관리합니다.

---

## Directory Structure

MVC 패턴을 준수하여 관심사를 분리한 패키지 구조

```text
hospital
 ├── src
 │    ├── main
 │    │    ├── java                     # Backend Logic
 │    │    │    └── com.kosa2.hospital
 │    │    │         ├── controller     # Request/Response Handling
 │    │    │         ├── service        # Business Logic & Transaction Management
 │    │    │         ├── dao            # Data Access Object (JdbcTemplate)
 │    │    │         ├── dto            # Data Transfer Object
 │    │    │         ├── model          # Domain Entity
 │    │    │         ├── enums          # Constant Values (Gender, Status, Grade)
 │    │    │         ├── interceptor    # Auth & Common Processing
 │    │    │         ├── util           # Configuration & Utilities
 │    │    │         └── HospitalApplication.java 
 │    │    │
 │    │    └── resources 
 │    │         ├── static              # Static Assets (CSS, JS)
 │    │         ├── templates           # View Templates (Thymeleaf)
 │    │         │    ├── layout         # Fragments (Header, Footer)
 │    │         │    ├── patients       # Patient Views
 │    │         │    ├── records        # EMR Views
 │    │         │    ├── reservations   # Reservation Views
 │    │         │    ├── doctors        # Staff Management Views
 │    │         │    ├── login          # Auth Views
 │    │         │    └── home           # Dashboard View
 │    │         ├── sql                 # Schema & Data Initialization
 │    │         └── application.properties 
 │    │
 │    └── test                          # Unit Tests
 │
 ├── build.gradle 
 └── README.md
```

---

