package com.erp.entity;

import com.erp.constant.enums.CommonStatus;
import com.erp.constant.enums.UserRole;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = "com.erp.utils.SnowflakeIdGenerator")
    private Long id;

    @Column(name = "user_name", length = 100, nullable = false)
    private String userName;

    @Column(name = "password", length = 128)
    private String password;

    @Column(name = "token")
    private String token;

    @Enumerated
    @Column(name = "role")
    private UserRole role;

    @Enumerated
    @Column(name = "status")
    private CommonStatus commonStatus;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "createdBy", column = @Column(name = "created_by")),
            @AttributeOverride(name = "createdOn", column = @Column(name = "created_on")),
            @AttributeOverride(name = "updatedBy", column = @Column(name = "updated_by")),
            @AttributeOverride(name = "updatedOn", column = @Column(name = "updated_on")),
            @AttributeOverride(name = "deleteBy", column = @Column(name = "delete_by"))

    })
    private AuditData auditData;

}
