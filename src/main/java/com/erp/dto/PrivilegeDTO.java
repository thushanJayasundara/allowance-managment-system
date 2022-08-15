package com.erp.dto;

import com.erp.constant.enums.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeDTO {

    private String id;
    private String privilege;
    private String privilegePolices;
    private String privilegeDescription;
    private CommonStatus commonStatus;

}
