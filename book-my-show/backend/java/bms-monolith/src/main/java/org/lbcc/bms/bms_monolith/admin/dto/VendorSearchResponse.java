package org.lbcc.bms.bms_monolith.admin.dto;

import lombok.AllArgsConstructor;
import org.lbcc.bms.bms_monolith.common.dto.AddressDto;
import org.lbcc.bms.bms_monolith.common.entity.Vendor;

import java.util.List;

@AllArgsConstructor
public class VendorSearchResponse {
    private List<VendorDto> vendorDtoList;

    public static VendorDto buildVendorDtoFromVendor(Vendor vendor) {
        VendorDto vendorDto = new VendorDto();
        vendorDto.setId(vendor.getId());
        vendorDto.setName(vendor.getName());
        vendorDto.setContactNumber(vendor.getContactNumber());
        vendorDto.setEmail(vendor.getEmail());
        vendorDto.setAddress(AddressDto.addressToAddressDto(vendor.getAddress()));
        vendorDto.setWebsite(vendor.getWebsite());
        vendorDto.setGstNo(vendor.getGstNo());
        vendorDto.setPanNo(vendor.getPanNo());
        vendorDto.setStatus(vendor.getStatus());
        vendorDto.setRegistrationDate(String.valueOf(vendor.getRegistrationDate()));
        vendorDto.setLogoUrl(vendor.getLogoUrl());
        return vendorDto;
    }

    public static List<VendorDto> buildVendorDtoListFromVendorList(List<Vendor> vendorList) {
        return vendorList.stream().map(VendorSearchResponse::buildVendorDtoFromVendor).toList();
    }
}
