package org.lbcc.bms.bms_monolith.common.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
@MappedSuperclass
@NoArgsConstructor
@Setter
@SuperBuilder
public abstract class BaseAuditingEntity {

    @Id
    private UUID id;

    private Instant createdDate;
    private Instant lastModifiedDate;

    private String createdBy;
    private String lastModifiedBy;
}
