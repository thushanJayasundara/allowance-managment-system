package com.erp.entity;

import com.erp.utils.LocalDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditData {

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createdOn;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime updatedOn;

    private Long createdBy;

    private Long updatedBy;

    private Long deleteBy;

}
