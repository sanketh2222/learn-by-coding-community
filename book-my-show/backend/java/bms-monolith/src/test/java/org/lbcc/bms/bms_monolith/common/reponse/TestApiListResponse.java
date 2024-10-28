package org.lbcc.bms.bms_monolith.common.reponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lbcc.bms.bms_monolith.common.response.ApiListResponse;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestApiListResponse {

    private Page<String> mockPage;

    @BeforeEach
    void setUp() {
        // Create a mock Page with sample data
        mockPage = Mockito.mock(Page.class);

        // Stub methods for the mock Page
        List<String> content = Arrays.asList("Item1", "Item2", "Item3");
        Mockito.when(mockPage.getContent()).thenReturn(content);
        Mockito.when(mockPage.getTotalElements()).thenReturn(100L);
        Mockito.when(mockPage.getTotalPages()).thenReturn(10);
        Mockito.when(mockPage.getNumber()).thenReturn(1); // Page numbers are 0-based by default
        Mockito.when(mockPage.getSize()).thenReturn(10);
        Mockito.when(mockPage.hasNext()).thenReturn(true);
        Mockito.when(mockPage.hasPrevious()).thenReturn(true);
    }

    @Test
    void testApiListResponseBuilder_setPage() {
        // Build the ApiListResponse using the builder and setPage method
        ApiListResponse<String> response = ApiListResponse.<String>builder()
                .success(true)
                .message("Test message")
                .setPage(mockPage)
                .build();

        // Assertions
        assertEquals("Test message", response.getMessage());
        assertTrue(response.isSuccess());
        assertEquals(Arrays.asList("Item1", "Item2", "Item3"), response.getData());
        assertEquals(100, response.getTotalItems());
        assertEquals(10, response.getTotalPages());
        assertEquals(1, response.getCurrentPage());
        assertEquals(10, response.getPageSize());
        assertTrue(response.isHasNextPage());
        assertTrue(response.isHasPreviousPage());
    }

    @Test
    void testJsonSerialization() throws JsonProcessingException {
        // Build the ApiListResponse using the builder and setPage method
        ApiListResponse<String> response = ApiListResponse.<String>builder()
                .success(true)
                .message("Test message")
                .setPage(mockPage)
                .build();

        // Configure ObjectMapper to handle Java 8 date/time types
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(response);

        // Assertions to check if the JSON string is not null and contains expected fields
        assertNotNull(json);
        assertTrue(json.contains("\"success\":true"));
        assertTrue(json.contains("\"message\":\"Test message\""));
        assertTrue(json.contains("\"totalItems\":100"));
        assertTrue(json.contains("\"totalPages\":10"));
        assertTrue(json.contains("\"currentPage\":1"));
        assertTrue(json.contains("\"pageSize\":10"));
        assertTrue(json.contains("\"hasNextPage\":true"));
        assertTrue(json.contains("\"hasPreviousPage\":true"));
        assertTrue(json.contains("\"data\":[\"Item1\",\"Item2\",\"Item3\"]"));
        assertTrue(json.contains("\"timestamp\"")); // timestamp field should be present
    }
}
