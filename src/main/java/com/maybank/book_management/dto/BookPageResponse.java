package com.maybank.book_management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class BookPageResponse<T> {
    private List<T> content = new ArrayList<>();
    private int page;
    private int size;
    private long totalElements;

        public BookPageResponse(Page<T> pageData) {
            this.content = pageData.getContent();
            this.page = pageData.getNumber();
            this.size = pageData.getSize();
            this.totalElements = pageData.getTotalElements();
        }
}