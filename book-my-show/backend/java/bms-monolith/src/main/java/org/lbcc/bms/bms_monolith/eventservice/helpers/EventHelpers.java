package org.lbcc.bms.bms_monolith.eventservice.helpers;

import lombok.extern.slf4j.Slf4j;
import org.lbcc.bms.bms_monolith.admin.dto.seat.SeatInShowDTO;
import org.lbcc.bms.bms_monolith.admin.dto.seat.SeatTypeInShowDTO;
import org.lbcc.bms.bms_monolith.admin.repository.SeatRepository;
import org.lbcc.bms.bms_monolith.common.entity.*;
import org.lbcc.bms.bms_monolith.common.enums.BookingStatus;
import org.lbcc.bms.bms_monolith.eventservice.dto.EventShowDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EventHelpers {

    private final Map<String, SeatType> seatTypeMap = new HashMap<>();
    private List<Seat> seats;

    public void updateSeatData(List<SeatType> seatTypes, List<Seat> seats) {
        seatTypes.forEach(seatType -> seatTypeMap.put(seatType.getId().toString(), seatType));
        this.seats = seats; // seats available are based on venue
    }

    public EventShow mapEventShowDTOToEntity(EventShowDTO eventShowDTO) {
        EventShow eventShow = EventShow.builder()
                .genres(eventShowDTO.getGenres())
                .startDate(eventShowDTO.getStartTime())
                .endDate(eventShowDTO.getEndTime())
                .build();

        List<SeatInShow> seatInShows = getSeatInShows();
        seatInShows.forEach(seat -> seat.setShow(eventShow));
        eventShow.setSeatInShows(seatInShows);

        //while onboarding admin can provide price for each seat type based on the show
        Map<String, BigDecimal> seatTypePriceMap = eventShowDTO.getSeatTypeInShows().stream()
                .collect(Collectors.toMap(SeatTypeInShowDTO::getSeatTypeId, SeatTypeInShowDTO::getPrice));

        List<SeatTypeInShow> seatTypesInShow = getSeatTypeInShows(seatTypePriceMap);
        seatInShows.forEach(seat -> seat.setSeatTypeInShow(getSeatTypeId(seat, seatTypesInShow)));
        seatTypesInShow.forEach(seatType -> seatType.setShow(eventShow));
        eventShow.setSeatTypeInShows(seatTypesInShow);

        return eventShow;
    }

    private List<SeatInShow> getSeatInShows() {
        return seats.stream()
                .map(EventHelpers::createSeatInShow)
                .collect(Collectors.toList());
    }

    private List<SeatType> getSeatTypes() {
        return new ArrayList<>(seatTypeMap.values());
    }

    private List<SeatTypeInShow> getSeatTypeInShows(Map<String, BigDecimal> seatTypePriceMap) {
        return getSeatTypes().stream()
                .map(seatType -> createSeatTypeInShow(seatTypePriceMap, seatType))
                .collect(Collectors.toList());
    }

    private SeatTypeInShow getSeatTypeId(SeatInShow seat, List<SeatTypeInShow> seatTypeInShows) {
        return seatTypeInShows.stream()
                .filter(seatType -> seatType.getSeatType().getId().equals(seat.getSeat().getSeatType().getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid seat type ID " + seat.getSeat().getSeatType().getId()));
    }

    //creation methods
    private SeatTypeInShow createSeatTypeInShow(Map<String, BigDecimal> seatTypePriceMap, SeatType seatType) {
        return SeatTypeInShow.builder()
                .seatType(seatType)
                .price(seatTypePriceMap.getOrDefault(seatType.getId().toString(), seatType.getPrice()))
                .build();
    }

    private static SeatInShow createSeatInShow(Seat seat) {
        return SeatInShow.builder()
                .seat(seat).bookingStatus(BookingStatus.AVAILABLE)
                .build();
    }

}