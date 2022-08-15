package com.erp.entity;

import com.erp.constant.enums.CommonStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GramaNiladhariDivision {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = "com.erp.utils.SnowflakeIdGenerator")
    private Long id;

    @Column(name = "division_Code")
    private String divisionCode;

    @Column(name = "grama_niladhari_division")
    private String gramaNiladhariDivision;

    @Enumerated
    @Column(name = "status",nullable = false)
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

    @OneToOne(mappedBy = "gramaNiladhariDivision")
    private Person person;
}
