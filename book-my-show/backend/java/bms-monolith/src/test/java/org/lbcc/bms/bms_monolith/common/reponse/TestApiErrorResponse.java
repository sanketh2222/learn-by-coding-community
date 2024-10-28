package org.lbcc.bms.bms_monolith.common.reponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.lbcc.bms.bms_monolith.common.response.ApiErrorResponse;

import java.io.IOException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class TestApiErrorResponse {

    @Test
    void testApiErrorResponseBuilder() {
        // Create ApiErrorResponse using builder
        ApiErrorResponse response = ApiErrorResponse.builder()
                .success(false)
                .message("An error occurred")
                .code("400_BAD_REQUEST")
                .build();

        // Verify the values
        assertFalse(response.isSuccess());
        assertEquals("An error occurred", response.getMessage());
        assertEquals("400_BAD_REQUEST", response.getCode());

        // Check that timestamp is not null and is recent
        assertNotNull(response.getTimestamp());
        assertTrue(response.getTimestamp().isBefore(Instant.now().plusSeconds(1)),
                "Timestamp should be set to a value close to the current time");
    }

    @Test
    void testJsonSerialization() throws IOException {
        // Create an instance of ApiErrorResponse
        ApiErrorResponse response = ApiErrorResponse.builder()
                .success(false)
                .message("Validation error")
                .code("400_BAD_REQUEST")
                .build();

        // Serialize to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(response);

        // Deserialize back to object
        ApiErrorResponse deserializedResponse = objectMapper.readValue(json, ApiErrorResponse.class);

        // Verify that values match
        assertEquals(response.isSuccess(), deserializedResponse.isSuccess());
        assertEquals(response.getMessage(), deserializedResponse.getMessage());
        assertEquals(response.getCode(), deserializedResponse.getCode());
        assertEquals(response.getTimestamp(), deserializedResponse.getTimestamp());
    }
}

