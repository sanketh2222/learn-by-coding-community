package org.lbcc.bms.bms_monolith.vendor.controller;

import org.lbcc.bms.bms_monolith.common.enums.VendorStatus;
import org.lbcc.bms.bms_monolith.vendor.dto.VendorOnboardRequestDto;
import org.lbcc.bms.bms_monolith.vendor.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/vendor")
@RestController
public class VendorOnboardingController {

    private final VendorService vendorService;

    @Autowired
    public VendorOnboardingController(VendorService vendorService) {
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
