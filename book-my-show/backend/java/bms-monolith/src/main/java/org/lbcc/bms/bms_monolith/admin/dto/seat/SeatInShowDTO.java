package org.lbcc.bms.bms_monolith.admin.dto.seat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.lbcc.bms.bms_monolith.common.enums.BookingStatus;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class SeatInShowDTO {

    private String seatId; // ID of the venue seat being used

    private BookingStatus bookingStatus; // Status of the booking

}
