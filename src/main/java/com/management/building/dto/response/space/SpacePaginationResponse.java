package com.management.building.dto.response.space;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpacePaginationResponse<T> {
    private List<T> data;
    private PaginationInfo pagination;
    private HierarchyMeta meta;

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PaginationInfo {
        private String nextCursor;
        private String prevCursor;
        private Boolean hasMore;
        private Integer totalLevels;
        private Integer currentPage;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class HierarchyMeta {
        Long rootId;
        Integer maxDepth;
        Integer totalNodes;
        String format; // "flat" or "nested"
    }
}
