package com.example.restapi.controller;

import com.example.restapi.dto.PaginatedBookResponse;
import com.example.restapi.model.Book;
import com.example.restapi.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    void getAllBooks_ReturnsPaginatedBookResponse() {
        // Arrange
        int limit = 10;
        int offset = 0;
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Test Book 1");
        
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Test Book 2");
        
        List<Book> books = Arrays.asList(book1, book2);
        Page<Book> page = new PageImpl<>(books, PageRequest.of(0, limit), books.size());
        
        when(bookService.getAllBooks(any())).thenReturn(page);

        // Act
        var response = bookController.getAllBooks(limit, offset, null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        PaginatedBookResponse bookResponse = response.getBody();
        assertNotNull(bookResponse);
        assertEquals(2, bookResponse.getTotal());
        assertEquals(offset, bookResponse.getOffset());
        assertEquals(limit, bookResponse.getLimit());
        assertEquals(2, bookResponse.getValue().size());
        assertEquals("Test Book 1", bookResponse.getValue().get(0).getTitle());
        assertEquals("Test Book 2", bookResponse.getValue().get(1).getTitle());
    }
}