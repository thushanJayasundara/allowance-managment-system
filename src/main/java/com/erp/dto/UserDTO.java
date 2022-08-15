package com.erp.dto;

import com.erp.constant.enums.CommonStatus;
import com.erp.constant.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    
    private String id;
    private String userName;
    private String password;
    private String token;
    private UserRole role;
    private CommonStatus commonStatus;
    
}
