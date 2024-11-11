package org.lbcc.bms.bms_monolith.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lbcc.bms.bms_monolith.common.entity.Vendor;
import org.lbcc.bms.bms_monolith.common.enums.VendorStatus;
import org.lbcc.bms.bms_monolith.common.response.ApiResponse;
import org.lbcc.bms.bms_monolith.admin.dto.VendorDto;
import org.lbcc.bms.bms_monolith.admin.dto.VendorOnboardRequestDto;
import org.lbcc.bms.bms_monolith.admin.dto.VendorOnboardResponseDto;
import org.lbcc.bms.bms_monolith.admin.dto.VendorSearchResponseDto;
import org.lbcc.bms.bms_monolith.admin.exceptions.InvalidVendorRequest;
import org.lbcc.bms.bms_monolith.admin.exceptions.VendorNotFoundException;
import org.lbcc.bms.bms_monolith.admin.repository.VendorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AdminService {

    private final VendorRepository vendorRepository;

    public AdminService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    public ApiResponse<VendorOnboardResponseDto> onboardNewVendor(VendorOnboardRequestDto vendorOnboardRequestDto) {
        log.info("Onboarding new vendor {}", vendorOnboardRequestDto.getName());
        //TODO: will save the logoFile to file storage like s3 and get the URL and then update the vendor object before saving
        Vendor vendor = vendorRepository.save(VendorOnboardRequestDto.buildVendorFromDto(vendorOnboardRequestDto));
        log.info("Vendor onboarded successfully with id {}", vendor.getId());

        return ApiResponse.<VendorOnboardResponseDto>builder()
                .success(true)
                .message("Vendor onboarded successfully")
                .data(new VendorOnboardResponseDto(vendor.getId().toString(), vendor.getName(), vendor.getStatus()))
                .build();
    }

    public List<VendorDto> searchVendor(String vendorName) {
        if (StringUtils.isEmpty(vendorName)) {
            throw new InvalidVendorRequest("Vendor name cannot be empty");
        }

        log.info("Searching for vendor with name {}", vendorName);
        List<Vendor> vendorList = vendorRepository.findByNameContaining(vendorName);
        if (vendorList.isEmpty()) {
            log.info("No vendor found with name {}", vendorName);
            return List.of();
        }
        log.info("Vendor found with name {}", vendorName);
        return VendorSearchResponseDto.buildVendorDtoListFromVendorList(vendorList);


    }

    public ApiResponse<String> updatedVendorStatus(String vendorId, VendorStatus vendorStatus) {
        validateVendorUpdateRequest(vendorId, vendorStatus);
        log.info("Suspending vendor with id {}", vendorId);
        Vendor vendor = vendorRepository.findById(UUID.fromString(vendorId)).orElseThrow(() -> new VendorNotFoundException("Vendor not found"));
        vendor.setStatus(vendorStatus);
        vendorRepository.save(vendor);
        log.info("Vendor id {}  suspended successfully", vendorId);
        return ApiResponse.<String>builder()
                .success(true)
                .message("Vendor status updated successfully")
                .data(vendorId)
                .build();
    }

    public Page<Vendor> getAllVendors(Pageable pageable) {
        return vendorRepository.findAll(pageable);
    }

    private void validateVendorUpdateRequest(String vendorId, VendorStatus vendorStatus) {
        if (vendorId == null || vendorId.isEmpty()) {
            throw new InvalidVendorRequest("Vendor id cannot be empty");
        }
        if (vendorStatus == null) {
            throw new InvalidVendorRequest("Vendor status cannot be empty");
        }
    }
}
