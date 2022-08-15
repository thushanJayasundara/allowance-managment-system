package com.erp.dto;

import com.erp.constant.enums.CommonStatus;
import com.erp.constant.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {

    private String id;
    private String fullName;
    private String initials;
    private String firstName;
    private String lastName;
    private String dob;
    private String registrationDate;
    private String nic;
    private String contactNumber;
    private String address;
    private Gender gender;
    private GramaNiladhariDivisionDTO gramaNiladhariDivisionDTO;
    private CommonStatus commonStatus;

}
