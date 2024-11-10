package org.lbcc.bms.bms_monolith.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lbcc.bms.bms_monolith.common.entity.BaseAuditingEntity;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventType extends BaseAuditingEntity {

    @Column(length = 15)
    private String label;
}
