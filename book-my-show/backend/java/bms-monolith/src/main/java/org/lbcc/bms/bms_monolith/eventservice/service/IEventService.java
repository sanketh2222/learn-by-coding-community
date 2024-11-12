package org.lbcc.bms.bms_monolith.eventservice.service;

import org.lbcc.bms.bms_monolith.eventservice.dto.EventResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IEventService {

    Page<EventResponse> getAllEvents(Pageable pageable);
}
