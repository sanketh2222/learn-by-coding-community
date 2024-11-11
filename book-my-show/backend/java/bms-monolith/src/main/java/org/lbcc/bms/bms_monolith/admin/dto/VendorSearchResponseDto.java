package org.lbcc.bms.bms_monolith.admin.dto;

import lombok.AllArgsConstructor;
import org.lbcc.bms.bms_monolith.common.entity.Vendor;

import java.util.List;

@AllArgsConstructor
public class VendorSearchResponseDto {
    private List<VendorDto> vendorDtoList;

    public static VendorDto buildVendorDtoFromVendor(Vendor vendor) {
        VendorDto vendorDto = new VendorDto();
        vendorDto.setId(vendor.getId());
        vendorDto.setName(vendor.getName());
        vendorDto.setContactNumber(vendor.getContactNumber());
        vendorDto.setEmail(vendor.getEmail());
        vendorDto.setAddress(vendor.getAddress());
        vendorDto.setWebsite(vendor.getWebsite());
        vendorDto.setGstNo(vendor.getGstNo());
        vendorDto.setPanNo(vendor.getPanNo());
        vendorDto.setStatus(vendor.getStatus());
        vendorDto.setRegistrationDate(vendor.getRegistrationDate());
        vendorDto.setLogoUrl(vendor.getLogoUrl());
        return vendorDto;
    }

    public static List<VendorDto> buildVendorDtoListFromVendorList(List<Vendor> vendorList) {
        return vendorList.stream().map(VendorSearchResponseDto::buildVendorDtoFromVendor).toList();
    }
}
