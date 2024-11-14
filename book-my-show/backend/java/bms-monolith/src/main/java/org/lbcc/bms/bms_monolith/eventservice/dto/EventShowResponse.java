package org.lbcc.bms.bms_monolith.eventservice.dto;

import lombok.Getter;
import org.lbcc.bms.bms_monolith.common.entity.EventShow;

import java.util.List;

@Getter
public class EventShowResponse {

    private List<String> genres;
    private String startDate;
    private String endDate;
    private List<SeatInShowResponse> seatInShows;
    private List<SeatTypeInShowResponse> seatTypeInShows;

    // Constructors, Getters, and Setters

    public EventShowResponse(List<String> genres, String startDate, String endDate,
                             List<SeatInShowResponse> seatInShows, List<SeatTypeInShowResponse> seatTypeInShows) {
        this.genres = genres;
        this.startDate = startDate;
        this.endDate = endDate;
        this.seatInShows = seatInShows;
        this.seatTypeInShows = seatTypeInShows;
    }

    // Static factory method for conversion from EventShow entity to EventShowResponse DTO
    public static EventShowResponse fromEntity(EventShow show) {
        return new EventShowResponse(
                show.getGenres() != null ? show.getGenres().stream()
                        .map(Enum::name)
                        .toList() : null,
                show.getStartDate().toString(),
                show.getEndDate().toString(),
                show.getSeatInShows() != null ? show.getSeatInShows().stream()
                        .map(SeatInShowResponse::fromEntity)
                        .toList() : null,
                show.getSeatTypeInShows() != null ? show.getSeatTypeInShows().stream()
                        .map(SeatTypeInShowResponse::fromEntity)
                        .toList() : null
        );
    }
}

