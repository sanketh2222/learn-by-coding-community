package org.lbcc.bms.bms_monolith.admin.dto.seat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.lbcc.bms.bms_monolith.common.entity.SeatTypeInShow;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class SeatTypeInShowDTO {
    private String seatTypeId; // ID of the seat type

    private String typeName; // E.g., Regular, VIP

    private BigDecimal price; // Price for this seat type
}
