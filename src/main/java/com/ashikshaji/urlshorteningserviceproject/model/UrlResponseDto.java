package com.ashikshaji.urlshorteningserviceproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlResponseDto {
    private String originalUrl;
    private String shortLink;
    private LocalDateTime expirationDate;
}
