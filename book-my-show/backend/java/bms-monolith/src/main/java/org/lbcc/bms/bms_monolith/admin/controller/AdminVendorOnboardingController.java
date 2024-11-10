package org.lbcc.bms.bms_monolith.admin.controller;

import org.lbcc.bms.bms_monolith.common.entity.Vendor;
import org.lbcc.bms.bms_monolith.common.enums.VendorStatus;
import org.lbcc.bms.bms_monolith.admin.dto.VendorOnboardRequestDto;
import org.lbcc.bms.bms_monolith.admin.service.AdminService;
import org.lbcc.bms.bms_monolith.common.response.ApiListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//TODO: all these endpoints will be allowed only by ADMIN

@RequestMapping("/vendor")
@RestController
public class AdminVendorOnboardingController {

    private final AdminService adminService;

    @Autowired
    public AdminVendorOnboardingController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/onboard")
    public ResponseEntity<?> onboardNewVendor(@RequestBody VendorOnboardRequestDto vendorOnboardRequestDto) {
        return ResponseEntity.ok(adminService.onboardNewVendor(vendorOnboardRequestDto));
    }

    @PutMapping("/suspend/{vendorId}")
    public ResponseEntity<?> suspendVendor(@PathVariable String vendorId) {
        return ResponseEntity.ok(adminService.updatedVendorStatus(vendorId, VendorStatus.SUSPENDED));
    }

    @PutMapping("/activate/{vendorId}")
    public ResponseEntity<?> activateVendor(@PathVariable String vendorId) {
        return ResponseEntity.ok(adminService.updatedVendorStatus(vendorId, VendorStatus.ACTIVE));
    }

    @GetMapping
    public ResponseEntity<ApiListResponse<Vendor>> getAllVendors(
            @PageableDefault(page = 0, size = 10)
            @SortDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.ok(ApiListResponse.<Vendor>builder()
                .success(true)
                .message("Vendors fetched successfully")
                .setPage(adminService.getAllVendors(pageable))
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchVendor(@RequestParam String vendorName) {
        return ResponseEntity.ok(adminService.searchVendor(vendorName));
    }
}
