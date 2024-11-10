package org.lbcc.bms.bms_monolith.admin.helpers;

import org.lbcc.bms.bms_monolith.admin.dto.VendorOnboardRequestDto;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VendorServiceTestHelper {

    public VendorOnboardRequestDto buildVendorOnboardRequest() {
        MultipartFile logoFile = new MockMultipartFile("logoFile", "logo.png", "image/png", "Sample logo".getBytes());

        return VendorOnboardRequestDto.builder()
                .name("Sample Vendor")
                .contactNumber("1234567890")
                .email("sample@example.com")
                .address("123, Sample Street")
                .website("https://www.sample.com")
                .gstNo("GST123456")
                .panNo("PAN123456")
                .registrationDate("12-12-2022")
                .logoFile(logoFile)
                .build();
    }

    public VendorOnboardRequestDto buildInvalidVendorOnboardRequest() {

        return VendorOnboardRequestDto.builder()
                .name("Sample Vendor")
                .contactNumber("1234567890")
                .email("sample@example.com")
                .address("123, Sample Street")
                .website("https://www.sample.com")
                .gstNo("GST123456")
                .panNo("PAN123456")
                .registrationDate("12-12-2022")
                .logoFile(null)
                .build();
    }
}
