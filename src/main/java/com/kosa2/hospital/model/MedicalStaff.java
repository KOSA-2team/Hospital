package com.kosa2.hospital.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalStaff {

    private Long medicalNum;
    private String medicalId;
    private String pwd;
    private String mname;
    private String specialty;
    private String mphone;
    private String email;
    private int power;
//      1: normal, 2: admin, 3: sys
}
