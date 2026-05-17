package com.groceryhub.util;

import com.groceryhub.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SuppressWarnings("null")
public class ResponseUtil {

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return success(data, "Operation successful");
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String code, String message, List<String> details) {
        ApiResponse.ErrorDetails error = ApiResponse.ErrorDetails.builder()
                .code(code)
                .message(message)
                .details(details)
                .build();

        ApiResponse<T> response = ApiResponse.<T>builder()
                .success(false)
                .error(error)
                .build();

        return ResponseEntity.status(status).body(response);
    }
}
