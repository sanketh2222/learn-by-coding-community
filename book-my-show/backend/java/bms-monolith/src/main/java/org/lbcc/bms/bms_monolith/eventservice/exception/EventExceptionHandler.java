package org.lbcc.bms.bms_monolith.eventservice.exception;

import org.lbcc.bms.bms_monolith.common.constants.BMSConstants;
import org.lbcc.bms.bms_monolith.common.response.ApiErrorResponse;
import org.lbcc.bms.bms_monolith.common.exception.GlobalExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EventExceptionHandler {

    @ExceptionHandler(EventServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleEventServiceException(EventServiceException ex) {
        return GlobalExceptionHandler.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), BMSConstants.EVENT_SERVICE_ERROR);
    }
}

