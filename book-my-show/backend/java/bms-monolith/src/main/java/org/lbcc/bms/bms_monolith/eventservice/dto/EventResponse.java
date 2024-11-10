package org.lbcc.bms.bms_monolith.eventservice.dto;

import org.lbcc.bms.bms_monolith.common.entity.Event;

import java.util.List;
import java.util.stream.Collectors;


public class EventResponse {

    private String title;
    private String description;
    private String vendorName;
    private String venueName;
    private List<EventShowResponse> shows;
    private String eventTypeName;
    private String startDate;
    private String endDate;
    private String thumbnailUrl;

    // Constructors, Getters, and Setters

    public EventResponse(String title, String description, String vendorName, String venueName,
                         List<EventShowResponse> shows, String eventTypeName, String startDate,
                         String endDate, String thumbnailUrl) {
        this.title = title;
        this.description = description;
        this.vendorName = vendorName;
        this.venueName = venueName;
        this.shows = shows;
        this.eventTypeName = eventTypeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static EventResponse fromEntity(Event event) {
        return new EventResponse(
                event.getTitle(),
                event.getDescription(),
                event.getVendor() != null ? event.getVendor().getName() : null,
                event.getVenue() != null ? event.getVenue().getName() : null,
                event.getShow() != null ? event.getShow().stream()
                        .map(EventShowResponse::fromEntity)
                        .collect(Collectors.toList()) : null,
                event.getEventType() != null ? event.getEventType().getLabel() : null,
                event.getStartDate().toString(),
                event.getEndDate().toString(),
                event.getThumbnailUrl()
        );
    }
}


