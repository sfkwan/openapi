package com.example.restapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Schema(description = "Paginated base response")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedBaseResponse<T> {

    @Schema(description = "Total number of items", example = "100")
    private long total;

    @Schema(description = "Offset of the current page", example = "0")
    private int offset;

    @Schema(description = "Limit of items per page", example = "10")
    private int limit;

    @Schema(description = "List of items")
    private List<T> value;

}
