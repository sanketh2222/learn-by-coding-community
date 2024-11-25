package org.lbcc.bms.bms_monolith.eventservice.helpers;

import lombok.extern.slf4j.Slf4j;
import org.lbcc.bms.bms_monolith.admin.dto.seat.SeatTypeInShowDTO;
import org.lbcc.bms.bms_monolith.common.entity.*;
import org.lbcc.bms.bms_monolith.common.enums.BookingStatus;
import org.lbcc.bms.bms_monolith.eventservice.dto.EventShowDTO;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class EventHelpers {

    public static EventShow mapEventShowDTOToEntity(EventShowDTO eventShowDTO) {
        EventShow eventShow = EventShow.builder()
                .genres(eventShowDTO.getGenres())
                .startDate(eventShowDTO.getStartTime())
                .endDate(eventShowDTO.getEndTime())
                .build();

        List<SeatInShow> seatInShows = getSeatInShows(eventShowDTO.getSeats());
        seatInShows.forEach(seat -> seat.setShow(eventShow));
        eventShow.setSeatInShows(seatInShows);

        //while onboarding admin can provide price for each seat type based on the show
        Map<String, BigDecimal> seatTypePriceMap = eventShowDTO.getSeatTypeInShows().stream()
                .collect(Collectors.toMap(SeatTypeInShowDTO::getSeatTypeId, SeatTypeInShowDTO::getPrice));

        List<SeatType> seatTypes = getSeatTypes(eventShowDTO.getSeatTypeMap());
        List<SeatTypeInShow> seatTypesInShow = getSeatTypeInShows(seatTypePriceMap, seatTypes);
        seatInShows.forEach(seat -> seat.setSeatTypeInShow(getSeatTypeId(seat, seatTypesInShow)));
        seatTypesInShow.forEach(seatType -> seatType.setShow(eventShow));
        eventShow.setSeatTypeInShows(seatTypesInShow);

        return eventShow;
    }

    private static List<SeatInShow> getSeatInShows(List<Seat> seats) {
        return seats.stream()
                .map(EventHelpers::createSeatInShow)
                .collect(Collectors.toList());
    }

    private static List<SeatType> getSeatTypes(Map<String, SeatType> seatTypeMap) {
        return new ArrayList<>(seatTypeMap.values());
    }

    private static List<SeatTypeInShow> getSeatTypeInShows(Map<String, BigDecimal> seatTypePriceMap, List<SeatType> seatTypes) {
        return seatTypes.stream()
                .map(seatType -> createSeatTypeInShow(seatTypePriceMap, seatType))
                .collect(Collectors.toList());
    }

    private static SeatTypeInShow getSeatTypeId(SeatInShow seat, List<SeatTypeInShow> seatTypeInShows) {
        return seatTypeInShows.stream()
                .filter(seatType -> seatType.getSeatType().getId().equals(seat.getSeat().getSeatType().getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid seat type ID " + seat.getSeat().getSeatType().getId()));
    }

    //creation methods
    private static SeatTypeInShow createSeatTypeInShow(Map<String, BigDecimal> seatTypePriceMap, SeatType seatType) {
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