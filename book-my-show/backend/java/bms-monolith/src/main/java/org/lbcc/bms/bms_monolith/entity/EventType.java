package org.lbcc.bms.bms_monolith.entity;

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

    private String label;
}
