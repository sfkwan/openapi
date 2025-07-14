package com.example.restapi.dto;

import com.example.restapi.model.Book;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;
import java.util.List;

@Schema(description = "Paginated response for books")
@NoArgsConstructor
public class PaginatedBookResponse extends PaginatedBaseResponse<Book> {
    
    public PaginatedBookResponse(long total, int offset, int limit, List<Book> value) {
        super(total, offset, limit, value);
    }
}