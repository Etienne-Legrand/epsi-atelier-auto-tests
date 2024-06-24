package com.epsi.crud.service;

import com.epsi.crud.model.Book;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private static final String DEFAULT_BOOKS_FILE_PATH = "books.json";
    private String booksFilePath;
    private List<Book> books = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

     // Constructor with custom file path
     public BookService(String booksFilePath) {
        this.booksFilePath = booksFilePath;
        loadBooks();
    }

    // Default constructor
    public BookService() {
        this(DEFAULT_BOOKS_FILE_PATH);
    }

    private void loadBooks() {
        try {
            File file = new File(booksFilePath);
            if (file.exists()) {
                books = objectMapper.readValue(file, new TypeReference<List<Book>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBooks() {
        try {
            objectMapper.writeValue(new File(booksFilePath), books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public Optional<Book> getBookById(Long id) {
        return books.stream().filter(book -> book.getBookid().equals(id)).findFirst();
    }

    public Book createBook(Book book) {
        if (book.getBookid() == null) {
            book.setBookid((long) (books.size() + 1));
        } else if (getBookById(book.getBookid()).isPresent()) {
            return null; // Book with the same ID already exists
        }
        books.add(book);
        saveBooks();
        return book;
    }

    public void deleteBook(Long id) {
        books.removeIf(book -> book.getBookid().equals(id));
        saveBooks();
    }

    public Optional<Book> updateBook(Long id, Book bookDetails) {
        Optional<Book> book = getBookById(id);
        if (book.isPresent()) {
            Book updatedBook = book.get();
            updatedBook.setBookname(bookDetails.getBookname());
            updatedBook.setAuthor(bookDetails.getAuthor());
            updatedBook.setPrice(bookDetails.getPrice());
            saveBooks();
            return Optional.of(updatedBook);
        } else {
            return Optional.empty();
        }
    }
}
