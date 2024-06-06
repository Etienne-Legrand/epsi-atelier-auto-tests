package com.epsi.crud.controller;

import com.epsi.crud.model.Book;
import com.epsi.crud.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        when(bookService.getAllBooks()).thenReturn(List.of());
    }

    @Test
    void testCreateBook() throws Exception {
        Book book = new Book();
        book.setBookname("Test Book");
        book.setAuthor("Test Author");
        book.setPrice(100);

        when(bookService.createBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookname").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.price").value(100));
    }

    @Test
    void testCreateBookWithExistingId() throws Exception {
        Book book = new Book();
        book.setBookid(1L);
        book.setBookname("Test Book");
        book.setAuthor("Test Author");
        book.setPrice(100);

        when(bookService.createBook(any(Book.class))).thenReturn(null);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isConflict());
    }

    @Test
    void testGetBookById() throws Exception {
        Book book = new Book();
        book.setBookid(1L);
        book.setBookname("Test Book");
        book.setAuthor("Test Author");
        book.setPrice(100);

        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookname").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.price").value(100));
    }

    @Test
    void testGetBookByIdNotFound() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateBook() throws Exception {
        Book book = new Book();
        book.setBookid(1L);
        book.setBookname("Updated Book");
        book.setAuthor("Updated Author");
        book.setPrice(150);

        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(Optional.of(book));

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookname").value("Updated Book"))
                .andExpect(jsonPath("$.author").value("Updated Author"))
                .andExpect(jsonPath("$.price").value(150));
    }

    @Test
    void testUpdateBookNotFound() throws Exception {
        Book book = new Book();
        book.setBookname("Updated Book");
        book.setAuthor("Updated Author");
        book.setPrice(150);

        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBook() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(new Book()));

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteBookNotFound() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNotFound());
    }
}
