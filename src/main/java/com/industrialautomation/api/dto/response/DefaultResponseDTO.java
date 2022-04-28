package com.industrialautomation.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultResponseDTO {
    private int code;
    private ResponseStatus status;
    private String message;
    private Object data;
    private PaginationDTO pagination;

    public DefaultResponseDTO(int code, ResponseStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public DefaultResponseDTO(int code, ResponseStatus status, String message, Object data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public DefaultResponseDTO(int code, ResponseStatus status, String message, Object data, PaginationDTO pagination) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
        this.pagination = pagination;
    }
}

