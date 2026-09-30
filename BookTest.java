package com.library;

import static org.junit.jupiter.api.Assertions.*;
import com.library.model.Book;
import org.junit.jupiter.api.Test;

class BookTest {
    @Test void bookWithCopiesIsAvailable() { assertTrue(new Book(1, "Clean Code", "Martin", 1, 3, 2).isAvailable()); }
    @Test void bookWithNoCopiesIsNotAvailable() { assertFalse(new Book(1, "Clean Code", "Martin", 1, 3, 0).isAvailable()); }
}
