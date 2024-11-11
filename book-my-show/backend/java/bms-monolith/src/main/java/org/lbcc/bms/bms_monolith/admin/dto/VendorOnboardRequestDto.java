package org.lbcc.bms.bms_monolith.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.lbcc.bms.bms_monolith.common.entity.Vendor;
import org.lbcc.bms.bms_monolith.common.enums.VendorStatus;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class VendorOnboardRequestDto {

    @NotBlank(message = "Vendor name is required")
    @Size(max = 30, message = "Vendor name must be at most 30 characters")
    private String name;

    @NotBlank(message = "Contact is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid contact number. Please provide a 10 digit number.")
    private String contactNumber;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email format. Please provide a valid email.")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Website is required")
    private String website;

    @NotBlank(message = "GST number is required")
    private String gstNo;

    @NotBlank(message = "PAN number is required")
    private String panNo;

    @NotBlank(message = "Registration date is required")
    @Pattern(regexp = "^[0-9]{2}-[0-9]{2}-[0-9]{4}$", message = "Invalid date format. Please use dd-MM-yyyy.")
    private String registrationDate;

    @NotNull(message = "Logo is required")
    private MultipartFile logoFile;

    public static Vendor buildVendorFromDto(VendorOnboardRequestDto vendorOnboardRequestDto) {
        return Vendor.builder()
                .name(vendorOnboardRequestDto.getName())
                .contactNumber(vendorOnboardRequestDto.getContactNumber())
                .email(vendorOnboardRequestDto.getEmail())
                .address(vendorOnboardRequestDto.getAddress())
                .website(vendorOnboardRequestDto.getWebsite())
                .gstNo(vendorOnboardRequestDto.getGstNo())
                .panNo(vendorOnboardRequestDto.getPanNo())
                .registrationDate(vendorOnboardRequestDto.getRegistrationDate())
                .logoUrl(vendorOnboardRequestDto.getLogoFile().getOriginalFilename())
                .status(VendorStatus.ACTIVE)
                .build();
    }
}