-- FK 체크 해제 (DROP 에러 방지)
SET FOREIGN_KEY_CHECKS = 0;

-- 기존 테이블 삭제 (순서 중요: 자식 -> 부모)
DROP TABLE IF EXISTS prescription;
DROP TABLE IF EXISTS treatment;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS patient;
DROP TABLE IF EXISTS medical_staff;

-- FK 체크 다시 설정
SET FOREIGN_KEY_CHECKS = 1;

-- 1. 의료진 테이블
CREATE TABLE medical_staff (
    medical_num INT NOT NULL AUTO_INCREMENT,
    medical_id VARCHAR(30) NOT NULL,
    pwd VARCHAR(255) NOT NULL,
    mname VARCHAR(20) NOT NULL,
    specialty VARCHAR(20) NOT NULL,
    mphone VARCHAR(15) NOT NULL,
    email VARCHAR(40) NOT NULL,
    power TINYINT NOT NULL DEFAULT 1 COMMENT '1:normal, 2:admin, 3:sys',
    PRIMARY KEY (medical_num),
    UNIQUE (medical_id),
    UNIQUE (email)
);

-- 2. 환자 테이블
CREATE TABLE patient (
    patient_num INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    birth_date DATE NOT NULL,
    gender TINYINT NOT NULL DEFAULT 0 COMMENT '0:unknown, 1:male, 2:female',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (patient_num),
    UNIQUE (phone)
);

-- 3. 예약 테이블
CREATE TABLE reservation (
    reservation_num INT NOT NULL AUTO_INCREMENT,
    patient_num INT NOT NULL,
    medical_num INT NOT NULL,
    reservation DATETIME NOT NULL,
    status TINYINT NOT NULL COMMENT '0:req, 1:confirmed, 2:canceled, 3:done',
    PRIMARY KEY (reservation_num),
    KEY IDX_Reservation_Patient (patient_num),
    KEY IDX_Reservation_Medical (medical_num),
    CONSTRAINT FK_Reservation_Patient FOREIGN KEY (patient_num) REFERENCES patient(patient_num),
    CONSTRAINT FK_Reservation_MedicalStaff FOREIGN KEY (medical_num) REFERENCES medical_staff(medical_num)
);

-- 4. 진료 기록 테이블
CREATE TABLE treatment (
    treatment_num INT NOT NULL AUTO_INCREMENT,
    reservation_num INT NOT NULL,
    diagnosis VARCHAR(300) NOT NULL,
    treatment VARCHAR(300) NOT NULL,
    treatment_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (treatment_num),
    CONSTRAINT FK_Treatment_Reservation FOREIGN KEY (reservation_num) REFERENCES reservation(reservation_num)
);

-- 5. 처방전 테이블
CREATE TABLE prescription (
    prescription_num INT NOT NULL AUTO_INCREMENT,
    treatment_num INT NOT NULL,
    medication_name VARCHAR(300) NOT NULL,
    dosage VARCHAR(300) NOT NULL,
    duration_days INT NOT NULL DEFAULT 1,
    PRIMARY KEY (prescription_num),
    CONSTRAINT FK_Prescription_Treatment FOREIGN KEY (treatment_num) REFERENCES treatment(treatment_num)
);