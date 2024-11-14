package org.lbcc.bms.bms_monolith.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lbcc.bms.bms_monolith.common.entity.Event;
import org.lbcc.bms.bms_monolith.eventservice.dto.EventResponse;
import org.lbcc.bms.bms_monolith.eventservice.exception.EventServiceException;
import org.lbcc.bms.bms_monolith.eventservice.repository.IEventRepository;
import org.lbcc.bms.bms_monolith.eventservice.service.impl.EventServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private IEventRepository IEventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void testGetAllEventsSuccess() {
        Event event = new Event();
        event.setTitle("Sample Event");
        event.setDescription("Sample Description");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Event> eventPage = new PageImpl<>(Collections.singletonList(event), pageable, 1);

        when(IEventRepository.findAll(pageable)).thenReturn(eventPage);

        Page<EventResponse> response = eventService.getAllEvents(pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals("Sample Event", response.getContent().get(0).getTitle());
        assertEquals("Sample Description", response.getContent().get(0).getDescription());
    }

    @Test
    void testGetAllEventsExceptionHandling() {
        when(IEventRepository.findAll(any(Pageable.class))).thenThrow(new RuntimeException("Database error"));

        Pageable pageable = PageRequest.of(0, 10);
        EventServiceException exception = assertThrows(EventServiceException.class, () ->
            eventService.getAllEvents(pageable)
        );

        assertEquals("Failed to fetch events", exception.getMessage());
    }
}
