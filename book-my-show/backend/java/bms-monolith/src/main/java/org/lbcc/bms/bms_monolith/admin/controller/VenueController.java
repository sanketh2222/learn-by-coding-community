package org.lbcc.bms.bms_monolith.admin.controller;

import org.lbcc.bms.bms_monolith.admin.dto.venue.VenueDto;
import org.lbcc.bms.bms_monolith.admin.dto.venue.VenueOnboardResponse;
import org.lbcc.bms.bms_monolith.admin.service.VenueService;
import org.lbcc.bms.bms_monolith.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/venues")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    //onboard venue
    @PostMapping("/onboard")
    public ResponseEntity<?> onboardVenue(@RequestBody VenueDto venueDto) {
        VenueOnboardResponse response = venueService.createVenue(venueDto);
        ApiResponse<VenueOnboardResponse> apiResponse = ApiResponse.<VenueOnboardResponse>builder()
                .success(true).message("Venue onboarded successfully")
                .data(response).build();
        return ResponseEntity.ok(apiResponse);
    }

    //add seats
    @PostMapping("/seats")
    public ResponseEntity<?> addSeatsToVenue(@RequestBody VenueDto venueDto) {
        VenueOnboardResponse response = venueService.createVenue(venueDto);
        ApiResponse<VenueOnboardResponse> apiResponse = ApiResponse.<VenueOnboardResponse>builder()
                .success(true).message("Seats added successfully")
                .data(response).build();
        return ResponseEntity.ok(apiResponse);
    }
}
