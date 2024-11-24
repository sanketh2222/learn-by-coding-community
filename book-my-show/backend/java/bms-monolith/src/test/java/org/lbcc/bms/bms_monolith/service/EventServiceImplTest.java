package org.lbcc.bms.bms_monolith.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lbcc.bms.bms_monolith.admin.dto.seat.SeatTypeInShowDTO;
import org.lbcc.bms.bms_monolith.admin.repository.SeatTypeRepository;
import org.lbcc.bms.bms_monolith.admin.repository.VendorRepository;
import org.lbcc.bms.bms_monolith.admin.service.AdminService;
import org.lbcc.bms.bms_monolith.admin.service.VenueService;
import org.lbcc.bms.bms_monolith.common.entity.*;
import org.lbcc.bms.bms_monolith.common.enums.Genre;
import org.lbcc.bms.bms_monolith.eventservice.dto.EventDTO;
import org.lbcc.bms.bms_monolith.eventservice.dto.EventResponse;
import org.lbcc.bms.bms_monolith.eventservice.dto.EventShowDTO;
import org.lbcc.bms.bms_monolith.eventservice.exception.EventServiceException;
import org.lbcc.bms.bms_monolith.eventservice.repository.EventTypeRepository;
import org.lbcc.bms.bms_monolith.eventservice.repository.IEventRepository;
import org.lbcc.bms.bms_monolith.eventservice.service.impl.EventServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {

    @Mock
    private IEventRepository IEventRepository;

    @Mock
    private EventTypeRepository eventTypeRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @Mock
    private AdminService adminService;

    @Mock
    private VenueService venueService;

    @Mock
    private SeatTypeRepository seatTypeRepository;


    @Test
    void testGetAllEventsSuccess() {
        Event event = new Event();
        event.setTitle("Sample Event");
        event.setDescription("Sample Description");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Event> eventPage = new PageImpl<>(Collections.singletonList(event), pageable, 1);

        when(IEventRepository.findAllWithDetails(pageable)).thenReturn(eventPage);

        Page<Event> response = eventService.getAllEvents(pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals("Sample Event", response.getContent().get(0).getTitle());
        assertEquals("Sample Description", response.getContent().get(0).getDescription());
    }

    @Test
    void testGetAllEventsExceptionHandling() {
        when(IEventRepository.findAllWithDetails(any(Pageable.class))).thenThrow(new RuntimeException("Database error"));

        EventServiceException exception = assertThrows(EventServiceException.class, () -> {
            eventService.getAllEvents(PageRequest.of(0, 10));
        });

        assertEquals("Failed to fetch events", exception.getMessage());
    }

    @Test
    void mapEventDTOToEntitySuccess() {
        EventDTO eventDTO = createEventDTO();

        Vendor vendor = new Vendor();
        vendor.setId(UUID.fromString(eventDTO.getVendorId()));

        List<Seat> seats = createSeats();
        Venue venue = new Venue();
        venue.setId(UUID.fromString(eventDTO.getVenueId()));
        venue.setSeats(seats);

        List<SeatType> seatTypes = createSeatTypes();

        EventType eventType = new EventType();
        eventType.setId(UUID.fromString(eventDTO.getEventTypeId()));

        when(adminService.findById(UUID.fromString(eventDTO.getVendorId()))).thenReturn(vendor);
        when(venueService.getVenueById(String.valueOf(UUID.fromString(eventDTO.getVenueId())))).thenReturn(venue);
        when(eventTypeRepository.findById(UUID.fromString(eventDTO.getEventTypeId()))).thenReturn(Optional.of(eventType));
        when(seatTypeRepository.findAll()).thenReturn(seatTypes);

        Event event = eventService.mapEventDTOToEntity(eventDTO);

        assertNotNull(event);
        assertEquals("Circus Festival 2024", event.getTitle());
        assertEquals("A two-day music festival featuring top artists.", event.getDescription());
        assertEquals(vendor, event.getVendor());
        assertEquals(venue, event.getVenue());
        assertEquals(eventType, event.getEventType());
    }

    @Test
    void mapEventDTOToEntityThrowsExceptionWhenVendorNotFound() {
        EventDTO eventDTO = createEventDTO();

        Venue venue = new Venue();
        venue.setId(UUID.fromString(eventDTO.getVenueId()));
        venue.setSeats(createSeats());

        when(adminService.findById(UUID.fromString(eventDTO.getVendorId()))).thenThrow(new IllegalArgumentException("Vendor not found"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            eventService.mapEventDTOToEntity(eventDTO);
        });

        assertEquals("Vendor not found", exception.getMessage());
    }

    @Test
    void mapEventDTOToEntityThrowsExceptionWhenVenueNotFound() {
        EventDTO eventDTO = createEventDTO();

        Vendor vendor = new Vendor();
        vendor.setId(UUID.fromString(eventDTO.getVendorId()));

        when(adminService.findById(UUID.fromString(eventDTO.getVendorId()))).thenReturn(vendor);
        when(venueService.getVenueById(String.valueOf(UUID.fromString(eventDTO.getVenueId())))).thenThrow(new IllegalArgumentException("Invalid venue ID"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            eventService.mapEventDTOToEntity(eventDTO);
        });

        assertEquals("Invalid venue ID", exception.getMessage());
    }

    private List<Seat> createSeats() {
        List<Seat> seats = new ArrayList<>();
        List<SeatType> seatTypes = createSeatTypes();
        Random random = new Random();

        seats.add(createSeat("A1", seatTypes.get(random.nextInt(seatTypes.size()))));
        seats.add(createSeat("A2", seatTypes.get(random.nextInt(seatTypes.size()))));
        seats.add(createSeat("B1", seatTypes.get(random.nextInt(seatTypes.size()))));
        seats.add(createSeat("B2", seatTypes.get(random.nextInt(seatTypes.size()))));

        return seats;
    }

    private Seat createSeat(String label, SeatType seatType) {
        Seat seat = new Seat();
        seat.setId(UUID.randomUUID());
        seat.setLabel(label);
        seat.setSeatType(seatType);
        return seat;
    }

    private SeatTypeInShowDTO createSeatTypeInShowDTO(String seatTypeId, BigDecimal price) {
        return SeatTypeInShowDTO.builder()
                .seatTypeId(seatTypeId)
                .price(price)
                .build();
    }

    private EventShowDTO createEventShowDTO(Instant showDate, Instant startTime, Instant endTime,
                                            List<Genre> genres, List<SeatTypeInShowDTO> seatTypeInShows) {
        return EventShowDTO.builder()
                .showDate(showDate)
                .startTime(startTime)
                .endTime(endTime)
                .genres(genres)
                .seatTypeInShows(seatTypeInShows)
                .build();
    }

    public EventDTO createEventDTO() {
        // Create seat types for the shows
        SeatTypeInShowDTO seatType1Show1 = createSeatTypeInShowDTO("2d72c22e-951d-42fc-b570-a5c99d6a7fc0", new BigDecimal("150.00"));
        SeatTypeInShowDTO seatType2Show1 = createSeatTypeInShowDTO("7fe67850-56a9-45ae-a0e4-c04cda82b895", new BigDecimal("250.00"));
        SeatTypeInShowDTO seatType1Show2 = createSeatTypeInShowDTO("7fe67850-56a9-45ae-a0e4-c04cda82b895", new BigDecimal("300.00"));

        // Create EventShowDTOs
        EventShowDTO show1 = createEventShowDTO(
                Instant.parse("2024-06-15T00:00:00.000Z"),
                Instant.parse("2024-06-15T10:00:00.000Z"),
                Instant.parse("2024-06-15T22:00:00.000Z"),
                Arrays.asList(Genre.COMEDY, Genre.DRAMA),
                Arrays.asList(seatType1Show1, seatType2Show1)
        );

        EventShowDTO show2 = createEventShowDTO(
                Instant.parse("2024-06-16T00:00:00.000Z"),
                Instant.parse("2024-06-16T10:00:00.000Z"),
                Instant.parse("2024-06-16T22:00:00.000Z"),
                Arrays.asList(Genre.ACTION, Genre.THRILLER),
                List.of(seatType1Show2)
        );

        // Create the main EventDTO
        return EventDTO.builder()
                .title("Circus Festival 2024")
                .description("A two-day music festival featuring top artists.")
                .vendorId("79984c71-6080-4251-adcc-254c8939a8bc")
                .venueId("8d05ee6c-2f69-4650-b5a5-0aa7ebfd9909")
                .eventTypeId("2172e49a-ba32-4e52-9814-6d6ed043c6ee")
                .startDate(Instant.parse("2024-06-15T00:00:00.000Z"))
                .endDate(Instant.parse("2024-06-16T23:59:59.000Z"))
                .shows(Arrays.asList(show1, show2))
                .build();
    }

    private SeatType createSeatType(String uuid, String label, BigDecimal price) {
        SeatType seatType = new SeatType();
        seatType.setId(UUID.fromString(uuid));
        seatType.setLabel(label);
        seatType.setPrice(price);
        return seatType;
    }

    private List<SeatType> createSeatTypes() {
        return List.of(
                createSeatType("2d72c22e-951d-42fc-b570-a5c99d6a7fc0", "Regular", BigDecimal.valueOf(100)),
                createSeatType("7fe67850-56a9-45ae-a0e4-c04cda82b895", "VIP", BigDecimal.valueOf(200)),
                createSeatType("1ad50f91-fcf1-4d80-b54e-fada5b6ad973", "VIP", BigDecimal.valueOf(300))
        );
    }


}
