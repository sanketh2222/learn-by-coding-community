package org.lbcc.bms.bms_monolith.vendor.dto;

import lombok.Data;

@Data
public class VendorSearchRequestDto {

    private String vendorName;
    private String vendorStatus;
    private String registrationDate;
}
