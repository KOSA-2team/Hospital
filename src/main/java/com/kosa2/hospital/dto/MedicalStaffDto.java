package com.kosa2.hospital.dto;

import lombok.Data;


@Data
public class MedicalStaffDto {
    private Long medicalNum;
    private String medicalId;
    private String pwd;
    private String mName;
    private String specialty;
    private String mPhone;
    private String email;
    private Integer power;

}
