package org.lbcc.bms.bms_monolith.admin.exception;

import lombok.extern.slf4j.Slf4j;
import org.lbcc.bms.bms_monolith.admin.controller.AdminVendorOnboardingController;
import org.lbcc.bms.bms_monolith.common.constants.BMSConstants;
import org.lbcc.bms.bms_monolith.common.exception.GlobalExceptionHandler;
import org.lbcc.bms.bms_monolith.common.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(assignableTypes = {AdminVendorOnboardingController.class})
public class VendorExceptionHandler {

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ResponseEntity<ApiErrorResponse> handleEventServiceException(Exception ex) {
        log.error("Exception occurred in vendor operation ", ex);
        return GlobalExceptionHandler.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), BMSConstants.EVENT_SERVICE_ERROR);
    }

    @ExceptionHandler(VendorNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleVendorNotFoundException(VendorNotFoundException ex) {
        log.error("Vendor not found ", ex);
        return GlobalExceptionHandler.buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), BMSConstants.VENDOR_NOT_FOUND);
    }

    @ExceptionHandler(InvalidVendorRequest.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidVendorRequest(InvalidVendorRequest ex) {
        log.error("Invalid vendor request ", ex);
        return GlobalExceptionHandler.buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), BMSConstants.INVALID_VENDOR_REQUEST);
    }
}
