# Data Structure & Transaction Flow

## Entity Relationship Diagram (ERD)
진료(Treatment)와 처방(Prescription)의 **1:N 관계**와, 예약(Reservation)과의 **1:1 식별 관계**를 시각화했습니다.

```mermaid
erDiagram
    RESERVATION ||--|| TREATMENT : "1:1 (Generates)"
    TREATMENT ||--|{ PRESCRIPTION : "1:N (Contains)"
    
    RESERVATION {
        long reservation_num PK
        long patient_num FK
        long medical_num FK
        int status "0:Req, 1:Conf, 2:Cancel, 3:Done"
    }

    TREATMENT {
        long treatment_num PK
        long reservation_num FK "식별 관계"
        string diagnosis
        string treatment_content
        datetime date
    }

    PRESCRIPTION {
        long prescription_num PK
        long treatment_num FK "외래키 (FK)"
        string medication_name
        string dosage
        int duration_days
    }
```

## Transaction Flow
진료 기록 저장 시, 트랜잭션이 어떻게 관리되는지 단계별로
시퀀스 다이어그램으로 나타냈습니다.

```mermaid
sequenceDiagram
    autonumber
    participant C as Controller (RecordController)
    participant S as Service (RecordService)
    participant T_Dao as TreatmentDao
    participant P_Dao as PrescriptionDao
    participant R_Dao as ReservationDao
    participant DB as MySQL Database

    Note over C, S: 1. 요청 수신 (DTO)
    C->>S: saveRecord(RecordFormDto)
    
    rect rgb(230, 240, 255)
        Note over S, DB: @Transactional Start (All or Nothing)
        
        %% 1단계: 진료 저장
        S->>T_Dao: insert(Treatment)
        T_Dao->>DB: INSERT INTO treatment...
        DB-->>T_Dao: Return Generated Key (PK: 100)
        T_Dao-->>S: Return treatmentNum (100)

        %% 2단계: 처방전 반복 저장 (1:N)
        loop For each Prescription in List
            S->>S: Set treatmentNum(100) to DTO
            S->>P_Dao: insert(Prescription)
            P_Dao->>DB: INSERT INTO prescription...
        end

        %% 3단계: 예약 상태 변경
        S->>R_Dao: updateStatus(reservationNum, COMPLETED)
        R_Dao->>DB: UPDATE reservation SET status=3...
    end
    
    S-->>C: Return Success
```