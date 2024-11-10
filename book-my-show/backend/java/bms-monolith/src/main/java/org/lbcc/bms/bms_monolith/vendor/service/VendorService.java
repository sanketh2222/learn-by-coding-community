package org.lbcc.bms.bms_monolith.vendor.service;

import lombok.extern.slf4j.Slf4j;
import org.lbcc.bms.bms_monolith.common.entity.Vendor;
import org.lbcc.bms.bms_monolith.common.enums.VendorStatus;
import org.lbcc.bms.bms_monolith.common.response.ApiResponse;
import org.lbcc.bms.bms_monolith.vendor.dto.VendorDto;
import org.lbcc.bms.bms_monolith.vendor.dto.VendorOnboardRequestDto;
import org.lbcc.bms.bms_monolith.vendor.dto.VendorOnboardResponseDto;
import org.lbcc.bms.bms_monolith.vendor.dto.VendorSearchResponseDto;
import org.lbcc.bms.bms_monolith.vendor.exception.InvalidVendorRequest;
import org.lbcc.bms.bms_monolith.vendor.exception.VendorNotFoundException;
import org.lbcc.bms.bms_monolith.vendor.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class VendorService {

    private final VendorRepository vendorRepository;

    @Autowired
    public VendorService(VendorRepository vendorRepository) {
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

    public ApiResponse<VendorSearchResponseDto> searchVendor(String vendorName) {
        if (vendorName == null || vendorName.isEmpty()) {
            throw new InvalidVendorRequest("Vendor name cannot be empty");
        }
        log.info("Searching for vendor with name {}", vendorName);
        List<Vendor> vendorList = vendorRepository.findByNameContaining(vendorName);
        if (vendorList.isEmpty()) {
            log.info("No vendor found with name {}", vendorName);
            return ApiResponse.<VendorSearchResponseDto>builder()
                    .success(false)
                    .message("No vendor found with given name")
                    .build();
        }
        log.info("Vendor found with name {}", vendorName);
        List<VendorDto> vendorDtos = VendorSearchResponseDto.buildVendorDtoListFromVendorList(vendorList);
        return ApiResponse.<VendorSearchResponseDto>builder()
                .success(true)
                .message("Vendor found successfully")
                .data(new VendorSearchResponseDto(vendorDtos))
                .build();

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

    private void validateVendorUpdateRequest(String vendorId, VendorStatus vendorStatus) {
        if (vendorId == null || vendorId.isEmpty()) {
            throw new InvalidVendorRequest("Vendor id cannot be empty");
        }
        if (vendorStatus == null) {
            throw new InvalidVendorRequest("Vendor status cannot be empty");
        }
    }
}
