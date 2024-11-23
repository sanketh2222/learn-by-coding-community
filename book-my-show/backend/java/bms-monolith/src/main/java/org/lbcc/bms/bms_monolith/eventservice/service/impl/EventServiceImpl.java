package org.lbcc.bms.bms_monolith.eventservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.lbcc.bms.bms_monolith.admin.repository.SeatTypeRepository;
import org.lbcc.bms.bms_monolith.admin.service.AdminService;
import org.lbcc.bms.bms_monolith.admin.service.VenueService;
import org.lbcc.bms.bms_monolith.common.entity.*;
import org.lbcc.bms.bms_monolith.eventservice.dto.*;
import org.lbcc.bms.bms_monolith.eventservice.exception.EventServiceException;
import org.lbcc.bms.bms_monolith.eventservice.helpers.EventHelpers;
import org.lbcc.bms.bms_monolith.eventservice.repository.EventTypeRepository;
import org.lbcc.bms.bms_monolith.eventservice.repository.IEventRepository;
import org.lbcc.bms.bms_monolith.eventservice.service.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class EventServiceImpl implements IEventService {

    private final IEventRepository IEventRepository;
    private final EventTypeRepository eventTypeRepository;
    private final SeatTypeRepository seatTypeRepository;
    private final VenueService venueService;
    private final EventHelpers eventHelpers;
    private final AdminService adminService;

    @Autowired
    public EventServiceImpl(IEventRepository IEventRepository,
                            EventTypeRepository eventTypeRepository,
                            SeatTypeRepository seatTypeRepository,
                            VenueService venueService,
                            AdminService adminService,
                            EventHelpers eventHelpers) {
        this.IEventRepository = IEventRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.seatTypeRepository = seatTypeRepository;
        this.venueService = venueService;
        this.adminService = adminService;
        this.eventHelpers = eventHelpers;
    }

    @Override
    public Page<Event> getAllEvents(Pageable pageable) {
        try {
            Page<Event> eventsPage = IEventRepository.findAllWithDetails(pageable);
            log.info("Fetched {} events with pageable {}", eventsPage.getTotalElements(), pageable);

            return eventsPage;
        } catch (Exception e) {
            log.error("Error fetching events: {}", e.getMessage());
            throw new EventServiceException("Failed to fetch events", e);
        }
    }

    @Override
    public EventResponse hostEvent(EventDTO request) {
        Event event = mapEventDTOToEntity(request);

        Event savedEvent = IEventRepository.save(event); //save the event
        log.info("Event hosted successfully: {}", savedEvent);
        return EventResponse.fromEntity(savedEvent);
    }

    private Event mapEventDTOToEntity(EventDTO eventDTO) {
        Vendor vendor = adminService.findById(UUID.fromString(eventDTO.getVendorId()));
        Venue venue = venueService.getVenueById(eventDTO.getVenueId());

        List<SeatType> seatTypes = seatTypeRepository.findAll(); //TODO: can be cached later
        eventHelpers.updateSeatData(seatTypes, venue.getSeats());

        EventType eventType = eventTypeRepository.findById(UUID.fromString(eventDTO.getEventTypeId()))
                .orElse(null);

        Event event = Event.builder()
                .title(eventDTO.getTitle()).description(eventDTO.getDescription())
                .vendor(vendor).venue(venue).eventType(eventType)
                .startDate(eventDTO.getStartDate()).endDate(eventDTO.getEndDate())
                .build();

        List<EventShow> eventShows = eventDTO.getShows().stream()
                .map(eventHelpers::mapEventShowDTOToEntity)
                .toList();

        eventShows.forEach(show -> show.setEvent(event));
        event.setShow(eventShows);

        return event;
    }

    @Override
    public EventResponse addShowsToEvent(EventDTO eventDTO) {
        Event event = IEventRepository.findById(UUID.fromString(eventDTO.getId()))
                .orElseThrow(() -> new EventServiceException("Event not found", new Throwable()));

        List<EventShow> eventShows = eventDTO.getShows().stream()
                .map(eventHelpers::mapEventShowDTOToEntity)
                .toList();

        eventShows.forEach(show -> show.setEvent(event));
        event.getShow().addAll(eventShows);

        Event updatedEvent = IEventRepository.save(event);
        log.info("Shows added to event successfully: {}", updatedEvent);
        return EventResponse.fromEntity(updatedEvent);
    }

    @Override
    public EventTypeResponse addEventTypes(EventTypeRequestDto eventTypeRequestDto) {
        log.info("Adding event types: {}", eventTypeRequestDto.getEventTypes());
        List<String> eventTypesList = eventTypeRequestDto.getEventTypes();
        List<EventType> eventTypes = EventTypeRequestDto.toEntities(eventTypesList);
        List<EventType> savedEventTypes = eventTypeRepository.saveAll(eventTypes);
        log.info("Event types added successfully: {}", savedEventTypes);
        return new EventTypeResponse(EventTypeDto.fromEntities(savedEventTypes));
    }
}
