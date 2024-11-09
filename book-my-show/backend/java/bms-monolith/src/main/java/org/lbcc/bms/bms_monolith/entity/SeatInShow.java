package org.lbcc.bms.bms_monolith.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lbcc.bms.bms_monolith.common.entity.BaseAuditingEntity;
import org.lbcc.bms.bms_monolith.entity.enums.BookingStatus;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatInShow extends BaseAuditingEntity {

    @ManyToOne
    @JoinColumn(name = "seat_type_in_show_id")
    private SeatTypeInShow seatTypeInShow;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private EventShow show;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
}
