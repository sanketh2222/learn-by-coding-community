package org.lbcc.bms.bms_monolith.eventservice.service;

import org.lbcc.bms.bms_monolith.common.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IEventService {

    Page<Event> getAllEvents(Pageable pageable);
}
