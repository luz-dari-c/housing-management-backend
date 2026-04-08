package com.backend.housing.domain.valueobjects;


import com.backend.housing.domain.exceptions.InvalidPaginationException;

import java.util.Objects;

public final class Pagination {

    private static final int MAX_PAGE_SIZE = 100;

    private final int page;
    private final int size;

    public Pagination(int page, int size) {
        validate(page, size);
        this.page = page;
        this.size = size;
    }

    private void validate(int page, int size) {

        if (page < 0) {
            throw new InvalidPaginationException("Page index cannot be negative");
        }

        if (size <= 0) {
            throw new InvalidPaginationException("Page size must be greater than zero");
        }

        if (size > MAX_PAGE_SIZE) {
            throw new InvalidPaginationException(
                    "Page size cannot exceed " + MAX_PAGE_SIZE
            );
        }
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public static Pagination of(int page, int size) {
        return new Pagination(page, size);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pagination that)) return false;
        return page == that.page && size == that.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, size);
    }

}
