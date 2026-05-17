package com.groceryhub.util;

import com.groceryhub.dto.response.PaginatedResponse;
import org.springframework.data.domain.Page;

public class PaginationUtil {

    public static <T> PaginatedResponse<T> buildPaginatedResponse(Page<T> pageResult) {
        PaginatedResponse.PaginationMeta meta = PaginatedResponse.PaginationMeta.builder()
                .page(pageResult.getNumber() + 1)
                .limit(pageResult.getSize())
                .totalItems(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .build();

        return PaginatedResponse.<T>builder()
                .items(pageResult.getContent())
                .pagination(meta)
                .build();
    }
}
