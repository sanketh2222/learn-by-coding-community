package org.lbcc.bms.bms_monolith.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Builder(builderClassName = "ApiResponseBuilder")
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ApiListResponse<T> {
    private boolean success;
    private String message;
    private List<T> data;
    @Builder.Default
    private Instant timestamp = Instant.now();

    // pagination metadata
    private int totalItems;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private boolean hasNextPage;
    private boolean hasPreviousPage;

    public static class ApiResponseBuilder<T> {
        public ApiResponseBuilder<T> setPage(Page<T> page) {
            this.data = page.getContent();
            this.totalItems = (int) page.getTotalElements();
            this.totalPages = page.getTotalPages();
            this.currentPage = page.getNumber();
            this.pageSize = page.getSize();
            this.hasNextPage = page.hasNext();
            this.hasPreviousPage = page.hasPrevious();
            return this;
        }
    }
}
