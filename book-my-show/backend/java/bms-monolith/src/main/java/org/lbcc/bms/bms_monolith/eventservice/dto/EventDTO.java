package org.lbcc.bms.bms_monolith.eventservice.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Data
public class EventDTO {

    private String id;

    @Size(min = 10, max = 50, message = "Title must be between 10 and 50 characters.")
    private String title;

    private String description;

    //vendor details
    private String vendorId;

    //venue details
    private String venueId;

    //available shows
    private List<EventShowDTO> shows;

    //event type details
    private String eventTypeId;

    private Instant startDate;

    private MultipartFile thumbnailFile;

    private Instant endDate;

    private List<SeatTypeInShowRequest> seatTypeInShows;

}
