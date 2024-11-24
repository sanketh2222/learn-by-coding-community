package org.lbcc.bms.bms_monolith.eventservice.dto.seat;

import lombok.Data;
import org.lbcc.bms.bms_monolith.common.entity.SeatTypeInShow;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class SeatTypeInShowResponse {

    private UUID seatTypeId;
    private String seatTypeName;  // Optional, if you want to include the seat type name
    private BigDecimal price;

    // Constructors, Getters, and Setters

    public SeatTypeInShowResponse(UUID seatTypeId, String seatTypeName, BigDecimal price) {
        this.seatTypeId = seatTypeId;
        this.seatTypeName = seatTypeName;
        this.price = price;
    }

    // Static factory method for conversion from SeatTypeInShow entity to SeatTypeInShowResponse DTO
    public static SeatTypeInShowResponse fromEntity(SeatTypeInShow seatTypeInShow) {
        return new SeatTypeInShowResponse(
                seatTypeInShow.getSeatType() != null ? seatTypeInShow.getSeatType().getId() : null,
                seatTypeInShow.getSeatType() != null ? seatTypeInShow.getSeatType().getLabel() : null,
                seatTypeInShow.getPrice()
        );
    }
}
