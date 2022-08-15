package com.erp.entity;

import com.erp.constant.enums.CommonStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonPrivilege {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = "com.erp.utils.SnowflakeIdGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToMany
    @JoinColumn(name = "privilege_id", nullable = false)
    private Set<Privilege> privileges;

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

}
