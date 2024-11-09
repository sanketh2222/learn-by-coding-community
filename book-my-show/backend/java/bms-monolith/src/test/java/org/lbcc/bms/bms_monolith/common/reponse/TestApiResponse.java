package org.lbcc.bms.bms_monolith.common.reponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.lbcc.bms.bms_monolith.common.response.ApiResponse;

import java.io.IOException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class TestApiResponse {

    @Test
    void testApiResponseBuilder() {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("SUCCESS")
                .data("Response Body")
                .build();

        assertTrue(response.isSuccess());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals("Response Body", response.getData());

        assertNotNull(response.getTimestamp());
        assertTrue(response.getTimestamp().isBefore(Instant.now().plusSeconds(1)),
                "Timestamp should be set to a value close to the current time");
    }

    @Test
    void testJsonSerialization() throws IOException {
        // Create an instance of ApiErrorResponse
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("SUCCESS")
                .data("Response Body")
                .build();

        // Serialize to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(response);

        // Deserialize back to object
        ApiResponse<String> deserializedResponse = objectMapper.readValue(
                json, new TypeReference<ApiResponse<String>>() {}
        );

        // Verify that values match
        assertEquals(response.isSuccess(), deserializedResponse.isSuccess());
        assertEquals(response.getMessage(), deserializedResponse.getMessage());
        assertEquals(response.getData(), deserializedResponse.getData());
        assertEquals(response.getTimestamp(), deserializedResponse.getTimestamp());
    }
}


