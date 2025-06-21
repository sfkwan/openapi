package com.example.restapi.service;

import com.example.restapi.exception.NotFoundException;
import com.example.restapi.exception.UnprocessableContentException;
import com.example.restapi.model.Book;
import com.example.restapi.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {

        return bookRepository.findAll();
    }

    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Optional<Book> getBookById(Long id) {
        if (id > 10) {
            throw new UnprocessableContentException("Book with id greater than 10 is not processable");
        }
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Book not found with id: " + id);
        }
        return bookRepository.findById(id);
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new NotFoundException("Book not found with id: " + id);
        }
    }
}