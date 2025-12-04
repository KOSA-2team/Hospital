package com.kosa2.hospital.dto;

import com.kosa2.hospital.enums.Gender;
import com.kosa2.hospital.model.Patient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDto {
    private Integer patient_num;
    private String name;
    private String phone;
    private LocalDate birth_date;
    private Gender gender;
    private LocalDateTime created_at;

    // [1] Model -> DTO 변환
    public static PatientDto from(Patient model) {
        return PatientDto.builder()
                .patient_num(model.getPatient_num())
                .name(model.getName())
                .phone(model.getPhone())
                .birth_date(model.getBirth_date())
                .gender(model.getGender())
                .created_at(model.getCreated_at())
                .build();
    }

    // [2] DTO -> Model 변환
    public Patient toModel() {
        return Patient.builder()
                .patient_num(this.patient_num)
                .name(this.name)
                .phone(this.phone)
                .birth_date(this.birth_date)
                .gender(this.gender)
                .created_at(this.created_at)
                .build();
    }
}