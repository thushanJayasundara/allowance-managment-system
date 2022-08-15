package com.erp.entity;

import com.erp.constant.enums.CommonStatus;
import com.erp.constant.enums.Gender;
import com.erp.utils.LocalDateTimeConverter;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = "com.erp.utils.SnowflakeIdGenerator")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "name_with_initials")
    private String initials;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "dob")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime dob;

    @Column(name = "reg_date")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime registrationDate;

    @Column(name = "NIC")
    private String nic;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "gender")
    private Gender gender;

    @Enumerated
    @Column(name = "status",nullable = false)
    private CommonStatus commonStatus;

    @OneToOne
    @JoinColumn(name = "grama_Niladhari_Division")
    private GramaNiladhariDivision gramaNiladhariDivision;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "createdBy", column = @Column(name = "created_by")),
            @AttributeOverride(name = "createdOn", column = @Column(name = "created_on")),
            @AttributeOverride(name = "updatedBy", column = @Column(name = "updated_by")),
            @AttributeOverride(name = "updatedOn", column = @Column(name = "updated_on")),
            @AttributeOverride(name = "deleteBy", column = @Column(name = "delete_by"))
    })
    private AuditData auditData;

    @OneToMany(mappedBy = "person")
    private Set<PersonPrivilege> personPrivileges;
}
