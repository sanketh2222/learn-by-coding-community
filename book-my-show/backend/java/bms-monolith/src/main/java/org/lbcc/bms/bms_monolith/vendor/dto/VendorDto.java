package org.lbcc.bms.bms_monolith.vendor.dto;

import lombok.Data;
import org.lbcc.bms.bms_monolith.common.entity.Vendor;
import org.lbcc.bms.bms_monolith.common.enums.VendorStatus;

import java.util.List;
import java.util.UUID;

@Data
public class VendorDto {

    private UUID id;
    private String name;
    private String contactNumber;
    private String email;
    private String address;
    private String website;
    private String gstNo;
    private String panNo;
    private VendorStatus status;
    private String registrationDate;
    private String logoUrl;
}
