package org.lbcc.bms.bms_monolith.admin.controller;

import org.lbcc.bms.bms_monolith.common.enums.VendorStatus;
import org.lbcc.bms.bms_monolith.admin.dto.VendorOnboardRequestDto;
import org.lbcc.bms.bms_monolith.admin.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/vendor")
@RestController
public class AdminVendorOnboardingController {

    private final VendorService vendorService;

    @Autowired
    public AdminVendorOnboardingController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    //TODO: this will be allowed only by ADMIN
    @PostMapping("/onboard")
    public ResponseEntity<?> onboardNewVendor(@RequestBody VendorOnboardRequestDto vendorOnboardRequestDto) {
        return ResponseEntity.ok(vendorService.onboardNewVendor(vendorOnboardRequestDto));
    }

    @PutMapping("/suspend/{vendorId}")
    public ResponseEntity<?> suspendVendor(@PathVariable String vendorId) {
        return ResponseEntity.ok(vendorService.updatedVendorStatus(vendorId, VendorStatus.SUSPENDED));
    }

    @PutMapping("/activate/{vendorId}")
    public ResponseEntity<?> activateVendor(@PathVariable String vendorId) {
        return ResponseEntity.ok(vendorService.updatedVendorStatus(vendorId, VendorStatus.ACTIVE));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchVendor(@RequestParam String vendorName) {
        return ResponseEntity.ok(vendorService.searchVendor(vendorName));
    }
}
