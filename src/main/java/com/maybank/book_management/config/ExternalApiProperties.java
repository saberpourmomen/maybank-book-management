package com.maybank.book_management.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Validated
@Component
@ConfigurationProperties(prefix = "external-api")
public class ExternalApiProperties {

    @NotNull
    private BookService bookService;

    @Getter
    @Setter
    public static class BookService {

        @NotBlank
        private String baseUrl;

        @Min(1)
        private int timeout;
    }
}