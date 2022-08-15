package com.erp.dto;

import com.erp.constant.enums.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonPrivilegeDTO {

    private String id;
    private PersonDTO person;
    private Set<PrivilegeDTO> privilege;
    private CommonStatus status;

}
