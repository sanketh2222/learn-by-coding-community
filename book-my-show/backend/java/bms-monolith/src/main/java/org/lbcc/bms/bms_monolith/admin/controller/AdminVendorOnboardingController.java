package org.lbcc.bms.bms_monolith.admin.controller;

import org.apache.commons.lang3.StringUtils;
import org.lbcc.bms.bms_monolith.admin.dto.VendorDto;
import org.lbcc.bms.bms_monolith.admin.dto.VendorOnboardResponse;
import org.lbcc.bms.bms_monolith.admin.dto.VendorSearchResponse;
import org.lbcc.bms.bms_monolith.common.entity.Vendor;
import org.lbcc.bms.bms_monolith.common.enums.VendorStatus;
import org.lbcc.bms.bms_monolith.admin.dto.VendorOnboardRequest;
import org.lbcc.bms.bms_monolith.admin.service.AdminService;
import org.lbcc.bms.bms_monolith.common.response.ApiListResponse;
import org.lbcc.bms.bms_monolith.common.response.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO: all these endpoints will be allowed only by ADMIN

@RequestMapping("/vendors")
@RestController
public class AdminVendorOnboardingController {

    private final AdminService adminService;

    public AdminVendorOnboardingController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/onboard")
    public ResponseEntity<ApiResponse<VendorOnboardResponse>> onboardNewVendor(@RequestBody VendorOnboardRequest vendorOnboardRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.onboardNewVendor(vendorOnboardRequest));
    }

    @PutMapping("/{vendorId}/{status}")
    public ResponseEntity<ApiResponse<String>> updateVendorStatus(@PathVariable String vendorId, @PathVariable VendorStatus status) {
        return ResponseEntity.ok(adminService.updatedVendorStatus(vendorId, status));
    }

    @GetMapping
    public ResponseEntity<?> getVendors(
            @RequestParam(required = false) String vendorName,
            @PageableDefault(page = 0, size = 10)
            @SortDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {

        if (StringUtils.isEmpty(vendorName)) {// Return paginated list of all vendors
            return ResponseEntity.ok(ApiListResponse.<Vendor>builder()
                    .success(true)
                    .message("Vendors fetched successfully")
                    .setPage(adminService.getAllVendors(pageable))
                    .build());
        } else {
            List<VendorDto> vendorsList = adminService.searchVendor(vendorName);// Search vendors by name
            boolean vendorsFound = !vendorsList.isEmpty();
            ApiResponse<VendorSearchResponse> response = ApiResponse.<VendorSearchResponse>builder()
                    .success(vendorsFound)
                    .message(vendorsFound ? "Vendors fetched successfully" : "No vendors found")
                    .data(vendorsFound ? new VendorSearchResponse(vendorsList) : null)
                    .build();
            return ResponseEntity.ok(response);
        }
    }

}
