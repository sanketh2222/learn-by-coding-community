package org.lbcc.bms.bms_monolith.admin.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lbcc.bms.bms_monolith.admin.dto.VendorOnboardRequest;
import org.lbcc.bms.bms_monolith.admin.dto.VendorOnboardResponse;
import org.lbcc.bms.bms_monolith.admin.exceptions.InvalidVendorRequest;
import org.lbcc.bms.bms_monolith.admin.exceptions.VendorNotFoundException;
import org.lbcc.bms.bms_monolith.admin.service.AdminService;
import org.lbcc.bms.bms_monolith.common.enums.VendorStatus;
import org.lbcc.bms.bms_monolith.common.response.ApiResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AdminVendorOnboardingControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminVendorOnboardingController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateVendorStatus_shouldReturnOk_whenVendorStatusUpdatedSuccessfully() {
        String vendorId = "123";
        VendorStatus status = VendorStatus.ACTIVE;
        ApiResponse<String> expectedResponse = ApiResponse.<String>builder()
                .success(true)
                .message("Vendor status updated successfully")
                .data(vendorId)
                .build();

        when(adminService.updatedVendorStatus(vendorId, status)).thenReturn(expectedResponse); //expected behaviour

        ResponseEntity<ApiResponse<String>> result = controller.updateVendorStatus(vendorId, status);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    void updateVendorStatus_shouldReturnError_whenVendorNotFound() {
        String vendorId = "123";
        VendorStatus status = VendorStatus.ACTIVE;

        when(adminService.updatedVendorStatus(vendorId, status)).thenThrow(new VendorNotFoundException("Vendor not found"));

        assertThrows(VendorNotFoundException.class, () -> {
            controller.updateVendorStatus(vendorId, status);
        });
    }

    @Test
    void updateVendorStatus_shouldReturnError_whenInvalidVendorRequest() {
        String vendorId = "";
        VendorStatus status = VendorStatus.ACTIVE;

        when(adminService.updatedVendorStatus(vendorId, status)).thenThrow(new InvalidVendorRequest("Vendor id cannot be empty"));

        assertThrows(InvalidVendorRequest.class, () -> {
            controller.updateVendorStatus(vendorId, status);
        });
    }


    //onboarding tests
    @Test
    void onboardNewVendor_shouldReturnCreated_whenVendorOnboardedSuccessfully() {
        VendorOnboardRequest request = VendorOnboardRequest.builder()
                .name("VendorName")
                .contactNumber("1234567890")
                .email("test@gmail.com").build();
        VendorOnboardResponse response = new VendorOnboardResponse("123", "VendorName", VendorStatus.ACTIVE);
        ApiResponse<VendorOnboardResponse> apiResponse = ApiResponse.<VendorOnboardResponse>builder()
                .success(true)
                .message("Vendor onboarded successfully")
                .data(response)
                .build();

        when(adminService.onboardNewVendor(request)).thenReturn(apiResponse);

        ResponseEntity<ApiResponse<VendorOnboardResponse>> result = controller.onboardNewVendor(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(apiResponse, result.getBody());
    }

    @Test
    void onboardNewVendor_shouldReturnError_whenInvalidVendorRequest() {
        VendorOnboardRequest request = VendorOnboardRequest.builder()
                .name("VendorName")
                .contactNumber("1234567890")
                .email("test@gmail.com").build();

        when(adminService.onboardNewVendor(request)).thenThrow(new InvalidVendorRequest("Invalid request"));

        assertThrows(InvalidVendorRequest.class, () -> {
            controller.onboardNewVendor(request);
        });
    }
}