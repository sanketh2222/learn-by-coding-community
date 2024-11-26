package org.lbcc.bms.bms_monolith.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lbcc.bms.bms_monolith.common.response.ApiListResponse;
import org.lbcc.bms.bms_monolith.common.entity.Event;
import org.lbcc.bms.bms_monolith.eventservice.controller.EventController;
import org.lbcc.bms.bms_monolith.eventservice.dto.EventResponse;
import org.lbcc.bms.bms_monolith.eventservice.exception.EventServiceException;
import org.lbcc.bms.bms_monolith.eventservice.service.IEventService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private IEventService eventService;

    @InjectMocks
    private EventController eventController;

    @Test
    void testGetAllEventsSuccess() {
        Event event = new Event();
        event.setId(UUID.randomUUID());
        event.setTitle("Sample Event");
        Pageable defaultPage = PageRequest.of(0, 10);
        Page<EventResponse> eventPage = new PageImpl<>(List.of(EventResponse.fromEntity(event)), defaultPage, 1);

        when(eventService.getAllEvents(any(Pageable.class))).thenReturn(eventPage);

        ResponseEntity<ApiListResponse<EventResponse>> response = eventController.getAllEvents(defaultPage);

        ApiListResponse<EventResponse> expectedResponse = ApiListResponse.<EventResponse>builder()
                .success(true)
                .message("Events fetched successfully")
                .data(eventPage.getContent())
                .totalItems((int) eventPage.getTotalElements())
                .totalPages(eventPage.getTotalPages())
                .currentPage(eventPage.getNumber())
                .pageSize(eventPage.getSize())
                .hasNextPage(eventPage.hasNext())
                .hasPreviousPage(eventPage.hasPrevious())
                .build();

        // Assertions to verify response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getMessage(), response.getBody().getMessage());
        assertEquals(expectedResponse.isSuccess(), response.getBody().isSuccess());
        assertEquals(1, response.getBody().getTotalItems());
    }

    @Test
    void testGetAllEventsInvalidPageParameters() {
        Page<EventResponse> emptyPage = Page.empty(PageRequest.of(0, 10));

        when(eventService.getAllEvents(PageRequest.of(0, 10))).thenReturn(emptyPage);

        ResponseEntity<ApiListResponse<EventResponse>> response = eventController.getAllEvents(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Events fetched successfully", response.getBody().getMessage());
        assertEquals(true, response.getBody().isSuccess());
    }

    @Test
    void testGetAllEventsExceptionHandling() {
        when(eventService.getAllEvents(any(Pageable.class)))
                .thenThrow(new EventServiceException("Failed to fetch events", new RuntimeException()));
        Pageable pageable = PageRequest.of(0, 10);
        EventServiceException exception = assertThrows(EventServiceException.class, () ->
            eventController.getAllEvents(pageable)
        );

        assertEquals("Failed to fetch events", exception.getMessage());
    }

}
