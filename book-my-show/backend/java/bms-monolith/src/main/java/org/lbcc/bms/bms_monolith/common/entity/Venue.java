package org.lbcc.bms.bms_monolith.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lbcc.bms.bms_monolith.common.enums.OperationalStatus;
import org.lbcc.bms.bms_monolith.common.enums.VenueType;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Venue extends BaseAuditingEntity {

    private String name;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer totalSeatingCapacity;

    @Enumerated(EnumType.STRING)
    private VenueType venueType;

    @Enumerated(EnumType.STRING)
    private OperationalStatus operationalStatus;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NotNull(message = "Seats list cannot be null.")
    @Size(min = 1, max = 100, message = "Seats list must contain between 1 and 100 seats.")
    private List<Seat> seats;
}
