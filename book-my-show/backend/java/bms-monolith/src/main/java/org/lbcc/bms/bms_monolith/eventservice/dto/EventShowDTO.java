package org.lbcc.bms.bms_monolith.eventservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lbcc.bms.bms_monolith.admin.dto.seat.SeatInShowDTO;
import org.lbcc.bms.bms_monolith.admin.dto.seat.SeatTypeInShowDTO;
import org.lbcc.bms.bms_monolith.common.entity.EventShow;
import org.lbcc.bms.bms_monolith.common.entity.SeatInShow;
import org.lbcc.bms.bms_monolith.common.entity.SeatTypeInShow;
import org.lbcc.bms.bms_monolith.common.enums.Genre;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventShowDTO {

    private String id;

    private Instant showDate;

    private Instant startTime;

    private Instant endTime;

    private List<Genre> genres;

    private List<SeatInShowDTO> seatInShows;
    private List<SeatTypeInShowDTO> seatTypeInShows;
}