package com.library.model;

public record Book(int id, String title, String author, int categoryId, int totalCopies, int availableCopies) {
    public boolean isAvailable() { return availableCopies > 0; }
}
