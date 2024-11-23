package org.lbcc.bms.bms_monolith.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.lbcc.bms.bms_monolith.common.enums.BookingStatus;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SeatInShow extends BaseAuditingEntity {

    @ManyToOne
    @JoinColumn(name = "seat_type_in_show_id")
    private SeatTypeInShow seatTypeInShow;

    @ManyToOne //fetch = FetchType.LAZY
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private EventShow show;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
}
