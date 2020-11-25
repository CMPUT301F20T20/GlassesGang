package com.example.glassesgang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookListTest {

    private BookList mockBookList() {
        BookList bookList = new BookList();
        bookList.addBook(mockBook());
        return bookList;
    }

    private Book mockBook() {
        return new Book("Lord of the Flies", "William Golding", "9780399501487", "testbid1", "testuser@gmail.com", "");
    }

    @Test
    void testAdd() {
        BookList bookList = mockBookList();

        assertEquals(1, bookList.getBooks().size());

        Book book = new Book("To Kill A Mockingbird", "Harper Lee", "9780446310789", "testbid2", "testuser@gmail.com", "");
        bookList.addBook(book);

        assertEquals(2, bookList.getBooks().size());
        assertTrue(bookList.getBooks().contains(book));
    }

    @Test
    void testRemove() {
        BookList bookList = mockBookList();

        assertEquals(1, bookList.getBooks().size());
        bookList.removeBook(mockBook());

        assertEquals(0, bookList.getBooks().size());
    }

    @Test
    void testClear() {
        BookList bookList = mockBookList();

        assertEquals(1, bookList.getBooks().size());

        bookList.clearBookList();

        assertEquals(0, bookList.getBooks().size());
    }


}
