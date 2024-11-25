package org.lbcc.bms.bms_monolith.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lbcc.bms.bms_monolith.admin.dto.*;
import org.lbcc.bms.bms_monolith.admin.exceptions.InvalidRequest;
import org.lbcc.bms.bms_monolith.common.entity.Vendor;
import org.lbcc.bms.bms_monolith.common.enums.VendorStatus;
import org.lbcc.bms.bms_monolith.common.response.ApiResponse;
import org.lbcc.bms.bms_monolith.admin.exceptions.VendorNotFoundException;
import org.lbcc.bms.bms_monolith.admin.repository.VendorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    public ApiResponse<VendorOnboardResponse> onboardNewVendor(VendorOnboardRequest vendorOnboardRequest) {
        log.info("Onboarding new vendor {}", vendorOnboardRequest.getName());
        //TODO: will save the logoFile to file storage like s3 and get the URL and then update the vendor object before saving
        Vendor vendor = vendorRepository.save(VendorOnboardRequest.buildVendorFromDto(vendorOnboardRequest));
        log.info("Vendor onboarded successfully with id {}", vendor.getId());

        return ApiResponse.<VendorOnboardResponse>builder()
                .success(true)
                .message("Vendor onboarded successfully")
                .data(new VendorOnboardResponse(vendor.getId().toString(), vendor.getName(), vendor.getStatus()))
                .build();
    }

    public List<VendorDto> searchVendor(String vendorName) {
        if (StringUtils.isEmpty(vendorName)) {
            throw new InvalidRequest("Vendor name cannot be empty");
        }

        log.info("Searching for vendor with name {}", vendorName);
        List<Vendor> vendorList = vendorRepository.findByNameContaining(vendorName);
        if (vendorList.isEmpty()) {
            log.info("No vendor found with name {}", vendorName);
            return List.of();
        }
        log.info("Vendor found with name {}", vendorName);
        return VendorSearchResponse.buildVendorDtoListFromVendorList(vendorList);


    }

    public Vendor findVendorById(UUID vendorId) {
        if (vendorId == null) {
            throw new InvalidRequest("Vendor id cannot be empty");
        }
        return vendorRepository.findById(vendorId).orElseThrow(() -> new VendorNotFoundException("Vendor not found"));
    }

    public ApiResponse<String> updatedVendorStatus(String vendorId, VendorStatus vendorStatus) {
        validateVendorUpdateRequest(vendorId, vendorStatus);
        log.info("Suspending vendor with id {}", vendorId);
        Vendor vendor = findVendorById(UUID.fromString(vendorId));
        vendor.setStatus(vendorStatus);
        vendorRepository.save(vendor);
        log.info("Vendor id {}  suspended successfully", vendorId);
        return ApiResponse.<String>builder()
                .success(true)
                .message("Vendor status updated successfully")
                .data(vendorId)
                .build();
    }

    public Page<Vendor> searchVendors(VendorSearchRequest searchRequest, Pageable pageable) {
        try {
            Specification<Vendor> spec = VendorSpecifications.createSpecification(searchRequest);
            return searchRequest.hasSearchCriteria() ? vendorRepository.findAll(spec, pageable)
                    : vendorRepository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error in searching vendors", e);
            return Page.empty();
        }
    }

    private void validateVendorUpdateRequest(String vendorId, VendorStatus vendorStatus) {
        if (StringUtils.isEmpty(vendorId)) {
            throw new InvalidRequest("Vendor id cannot be empty");
        }
        if (vendorStatus == null || StringUtils.isEmpty(vendorStatus.name())) {
            throw new InvalidRequest("Vendor status cannot be empty");
        }
    }
}
