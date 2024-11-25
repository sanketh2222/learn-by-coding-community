package org.lbcc.bms.bms_monolith.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatType extends BaseAuditingEntity {

    @Column(length = 15, nullable = false)
    private String label;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

}
