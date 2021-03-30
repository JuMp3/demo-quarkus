package it.jump3.controller.model;

import lombok.Data;

@Data
public class PaginatedResponse {

    private int totalPages;
    private long totalElements;
}
