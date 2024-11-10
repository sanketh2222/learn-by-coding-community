package org.lbcc.bms.bms_monolith.admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.lbcc.bms.bms_monolith.common.entity.Vendor;
import org.lbcc.bms.bms_monolith.common.enums.VendorStatus;
import org.lbcc.bms.bms_monolith.common.response.ApiResponse;

import org.lbcc.bms.bms_monolith.admin.dto.VendorOnboardRequestDto;
import org.lbcc.bms.bms_monolith.admin.dto.VendorOnboardResponseDto;
import org.lbcc.bms.bms_monolith.admin.dto.VendorSearchResponseDto;
import org.lbcc.bms.bms_monolith.admin.exception.InvalidVendorRequest;
import org.lbcc.bms.bms_monolith.admin.exception.VendorNotFoundException;
import org.lbcc.bms.bms_monolith.admin.helpers.VendorServiceTestHelper;
import org.lbcc.bms.bms_monolith.admin.repository.VendorRepository;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VendorServiceTest {

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private VendorService vendorService;

    private Vendor vendor;

    @Autowired
    private VendorServiceTestHelper vendorServiceTestHelper;

    @BeforeEach
    void setUp() {
        vendor = new Vendor();
        vendor.setId(UUID.randomUUID());
        vendor.setName("Test Vendor");
    }

    @Test
    @DisplayName("Search vendor with valid name returns vendor list")
    void searchVendorWithValidNameReturnsVendorList() {
        when(vendorRepository.findByNameContaining("Test Vendor")).thenReturn(List.of(vendor));

        ApiResponse<VendorSearchResponseDto> response = vendorService.searchVendor("Test Vendor");

        assertTrue(response.isSuccess());
        assertEquals("Vendor found successfully", response.getMessage());
        assertNotNull(response.getData());
        verify(vendorRepository, times(1)).findByNameContaining("Test Vendor");
    }

    @Test
    @DisplayName("Search vendor with empty name throws InvalidVendorRequest")
    void searchVendorWithEmptyNameThrowsInvalidVendorRequest() {
        assertThrows(InvalidVendorRequest.class, () -> vendorService.searchVendor(""));
    }

    @Test
    @DisplayName("Search vendor with null name throws InvalidVendorRequest")
    void searchVendorWithNullNameThrowsInvalidVendorRequest() {
        assertThrows(InvalidVendorRequest.class, () -> vendorService.searchVendor(null));
    }

    @Test
    @DisplayName("Search vendor with non-existing name returns no vendor found")
    void searchVendorWithNonExistingNameReturnsNoVendorFound() {
        when(vendorRepository.findByNameContaining("Non Existing Vendor")).thenReturn(Collections.emptyList());

        ApiResponse<VendorSearchResponseDto> response = vendorService.searchVendor("Non Existing Vendor");

        assertFalse(response.isSuccess());
        assertEquals("No vendor found with given name", response.getMessage());
        assertNull(response.getData());
        verify(vendorRepository, times(1)).findByNameContaining("Non Existing Vendor");
    }

    @Test
    @DisplayName("Update vendor status with valid id and status updates successfully")
    void updateVendorStatusWithValidIdAndStatusUpdatesSuccessfully() {
        UUID vendorId = UUID.randomUUID();
        Vendor vendor = new Vendor();
        vendor.setId(vendorId);
        vendor.setStatus(VendorStatus.ACTIVE);

        when(vendorRepository.findById(vendorId)).thenReturn(Optional.of(vendor));

        ApiResponse<String> response = vendorService.updatedVendorStatus(vendorId.toString(), VendorStatus.SUSPENDED);

        assertTrue(response.isSuccess());
        assertEquals("Vendor status updated successfully", response.getMessage());
        assertEquals(vendorId.toString(), response.getData());
        verify(vendorRepository, times(1)).findById(vendorId);
        verify(vendorRepository, times(1)).save(vendor);
    }

    @Test
    @DisplayName("Update vendor status with null id throws InvalidVendorRequest")
    void updateVendorStatusWithNullIdThrowsInvalidVendorRequest() {
        assertThrows(InvalidVendorRequest.class, () -> vendorService.updatedVendorStatus(null, VendorStatus.SUSPENDED));
    }

    @Test
    @DisplayName("Update vendor status with empty id throws InvalidVendorRequest")
    void updateVendorStatusWithEmptyIdThrowsInvalidVendorRequest() {
        assertThrows(InvalidVendorRequest.class, () -> vendorService.updatedVendorStatus("", VendorStatus.SUSPENDED));
    }

    @Test
    @DisplayName("Update vendor status with null status throws InvalidVendorRequest")
    void updateVendorStatusWithNullStatusThrowsInvalidVendorRequest() {
        UUID vendorId = UUID.randomUUID();
        assertThrows(InvalidVendorRequest.class, () -> vendorService.updatedVendorStatus(vendorId.toString(), null));
    }

    @Test
    @DisplayName("Update vendor status with non-existing id throws VendorNotFoundException")
    void updateVendorStatusWithNonExistingIdThrowsVendorNotFoundException() {
        UUID vendorId = UUID.randomUUID();
        when(vendorRepository.findById(vendorId)).thenReturn(Optional.empty());

        assertThrows(VendorNotFoundException.class, () -> vendorService.updatedVendorStatus(vendorId.toString(), VendorStatus.SUSPENDED));
        verify(vendorRepository, times(1)).findById(vendorId);
    }

    @Test
    @DisplayName("Onboard new vendor with valid request returns success response")
    void onboardNewVendorWithValidRequestReturnsSuccessResponse() {
        VendorOnboardRequestDto requestDto = vendorServiceTestHelper.buildVendorOnboardRequest();
        Vendor vendor = new Vendor();
        vendor.setId(UUID.randomUUID());
        vendor.setName("New Vendor");
        vendor.setStatus(VendorStatus.ACTIVE);

        when(vendorRepository.save(any(Vendor.class))).thenReturn(vendor);

        ApiResponse<VendorOnboardResponseDto> response = vendorService.onboardNewVendor(requestDto);

        assertTrue(response.isSuccess());
        assertEquals("Vendor onboarded successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(vendor.getId().toString(), response.getData().getVendorId());
        verify(vendorRepository, times(1)).save(any(Vendor.class));
    }

}
