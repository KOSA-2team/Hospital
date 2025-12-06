-- 실행 Cmd + Enter

-- 1. 데이터베이스(방)가 없으면 만들어라! (이게 핵심)
CREATE DATABASE IF NOT EXISTS hospital_db;

-- 2. 지금부터 이 방을 쓰겠다!
USE hospital_db;

CREATE TABLE IF NOT EXISTS `medical_staff` (
    `medical_num` INT NOT NULL AUTO_INCREMENT,
    `medical_id` VARCHAR(30) NOT NULL,
    `pwd` VARCHAR(255) NOT NULL,
    `mname` VARCHAR(20) NOT NULL,
    `specialty` VARCHAR(20) NOT NULL,
    `mphone` VARCHAR(15) NOT NULL,
    `email` VARCHAR(40) NOT NULL,
    `power` TINYINT NOT NULL DEFAULT 1 COMMENT '1:normal, 2:admin, 3:sys',
    PRIMARY KEY (`medical_num`),
    UNIQUE (`medical_id`),
    UNIQUE (`email`)
);

CREATE TABLE IF NOT EXISTS `patient` (
    `patient_num` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(20) NOT NULL,
    `phone` VARCHAR(15) NOT NULL,
    `birth_date` DATE NOT NULL,
    `gender` TINYINT NOT NULL DEFAULT 0 COMMENT '0:unknown, 1:male, 2:female',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`patient_num`),
    UNIQUE (`phone`)
);

CREATE TABLE IF NOT EXISTS `Reservation` (
    `reservation_num` INT NOT NULL AUTO_INCREMENT,
    `patient_num` INT NOT NULL,
    `medical_num` INT NOT NULL,
    `reservation` DATETIME NOT NULL,
    `status` TINYINT NOT NULL COMMENT '0:req, 1:confirmed, 2:canceled, 3:done',
    PRIMARY KEY (`reservation_num`),
    KEY `IDX_Reservation_Patient` (`patient_num`),
    KEY `IDX_Reservation_Medical` (`medical_num`),
    CONSTRAINT `FK_Reservation_Patient` FOREIGN KEY (`patient_num`) REFERENCES `patient`(`patient_num`),
    CONSTRAINT `FK_Reservation_MedicalStaff` FOREIGN KEY (`medical_num`) REFERENCES `medical_staff`(`medical_num`)
);

CREATE TABLE IF NOT EXISTS `Treatment` (
    `treatment_num` INT NOT NULL AUTO_INCREMENT,
    `reservation_num` INT NOT NULL,
    `diagnosis` VARCHAR(300) NOT NULL,
    `treatment` VARCHAR(300) NOT NULL,
    `treatment_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`treatment_num`),
    CONSTRAINT `FK_Treatment_Reservation` FOREIGN KEY (`reservation_num`) REFERENCES `Reservation`(`reservation_num`)
);

CREATE TABLE IF NOT EXISTS `prescription` (
    `prescription_num` INT NOT NULL AUTO_INCREMENT,
    `treatment_num` INT NOT NULL,
    `medication_name` VARCHAR(300) NOT NULL,
    `dosage` VARCHAR(300) NOT NULL,
    `duration_days` INT NOT NULL DEFAULT 1,
    PRIMARY KEY (`prescription_num`),
    CONSTRAINT `FK_Prescription_Treatment` FOREIGN KEY (`treatment_num`) REFERENCES `Treatment`(`treatment_num`)
);

-- 1. 의료진 테이블 속성 확인
DESC medical_staff;

-- 2. 환자 테이블 속성 확인
DESC patient;

-- 3. 예약 테이블 속성 확인
DESC Reservation;

-- 4. 진료 테이블 속성 확인
DESC Treatment;

-- 5. 처방전 테이블 속성 확인
DESC prescription;



-- 1. 의료진 전체 데이터 조회
SELECT * FROM medical_staff;

-- 2. 환자 전체 데이터 조회
SELECT * FROM patient;

-- 3. 예약 전체 데이터 조회
SELECT * FROM Reservation;

-- 4. 진료 전체 데이터 조회
SELECT * FROM Treatment;

-- 5. 처방전 전체 데이터 조회
SELECT * FROM prescription;


