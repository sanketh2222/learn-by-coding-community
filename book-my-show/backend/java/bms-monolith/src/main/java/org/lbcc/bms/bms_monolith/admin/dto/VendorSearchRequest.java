package org.lbcc.bms.bms_monolith.admin.dto;

import lombok.Data;

@Data
public class VendorSearchRequest {

    private String vendorName;
    private String vendorStatus;
    private String registrationDate;
}
