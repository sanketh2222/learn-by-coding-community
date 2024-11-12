package org.lbcc.bms.bms_monolith.eventservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.lbcc.bms.bms_monolith.common.entity.Event;
import org.lbcc.bms.bms_monolith.eventservice.dto.EventResponse;
import org.lbcc.bms.bms_monolith.eventservice.exception.EventServiceException;
import org.lbcc.bms.bms_monolith.eventservice.repository.IEventRepository;
import org.lbcc.bms.bms_monolith.eventservice.service.IEventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventServiceImpl implements IEventService {

    private final IEventRepository IEventRepository;

    public EventServiceImpl(IEventRepository IEventRepository) {
        this.IEventRepository = IEventRepository;
    }

    @Override
    public Page<EventResponse> getAllEvents(Pageable pageable) {
        try {
            Page<Event> eventsPage = IEventRepository.findAll(pageable);
            log.info("Fetched {} events with pageable {}", eventsPage.getTotalElements(), pageable);

            return eventsPage.map(EventResponse::fromEntity);
        } catch (Exception e) {
            log.error("Error fetching events: {}", e.getMessage());
            throw new EventServiceException("Failed to fetch events", e);
        }
    }
}
