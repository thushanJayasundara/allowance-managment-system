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
public class GramaNiladhariDivisionDTO {

    private String id;
    private String divisionCode;
    private String gramaNiladhariDivision;
    private CommonStatus commonStatus;
}
