package com.industrialautomation.api.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class PaginationDTO {
    private long count;
    private int limit;
    private int offset;

    public PaginationDTO(long count, int limit, int offset) {
        this.count = count;
        this.limit = limit;
        this.offset = offset;
    }
}
