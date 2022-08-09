package com.ashikshaji.urlshorteningserviceproject.controller;

import com.ashikshaji.urlshorteningserviceproject.model.Url;
import com.ashikshaji.urlshorteningserviceproject.model.UrlDto;
import com.ashikshaji.urlshorteningserviceproject.model.UrlErrorResponseDto;
import com.ashikshaji.urlshorteningserviceproject.model.UrlResponseDto;
import com.ashikshaji.urlshorteningserviceproject.service.UrlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController
public class UrlShorteningController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) {
        Url urlToReturn = urlService.generateShortLink(urlDto);

        if (urlToReturn != null) {
            UrlResponseDto urlResponseDto = new UrlResponseDto();
            urlResponseDto.setOriginalUrl(urlToReturn.getOriginalUrl());
            urlResponseDto.setExpirationDate(urlToReturn.getExpirationDate());
            urlResponseDto.setShortLink(urlToReturn.getShortLink());
            return new ResponseEntity<UrlResponseDto>(urlResponseDto, HttpStatus.OK);
        }

        UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
        urlErrorResponseDto.setStatus("404");
        urlErrorResponseDto.setError("Error occurred, please try again");
        return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink, HttpServletResponse response) throws IOException {

        if (StringUtils.isEmpty(shortLink)) {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setStatus("404");
            urlErrorResponseDto.setError("Invalid Url");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.NOT_FOUND);
        }

        Url urlToReturn = urlService.getEncodedUrl(shortLink);

        if (urlToReturn == null) {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setStatus("404");
            urlErrorResponseDto.setError("Url does not exists");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.NOT_FOUND);
        }

        if (urlToReturn.getExpirationDate().isBefore(LocalDateTime.now())) {
            urlService.deleteShortLink(urlToReturn);
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setStatus("404");
            urlErrorResponseDto.setError("Url Expired");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.NOT_FOUND);
        }

        response.sendRedirect(urlToReturn.getOriginalUrl());
        urlService.incrementOpenedCount(urlToReturn.getShortLink());
        return null;
    }

    @GetMapping("/viewOpenedCount/{shortLink}")
    public ResponseEntity<?> viewOpenedCount(@PathVariable String shortLink) {

        if (StringUtils.isEmpty(shortLink)) {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setStatus("404");
            urlErrorResponseDto.setError("Invalid Url");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.NOT_FOUND);
        }

        Url urlToReturn = urlService.getEncodedUrl(shortLink);

        if (urlToReturn == null) {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setStatus("404");
            urlErrorResponseDto.setError("Url does not exists");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>("Number of times url opened is " + urlToReturn.getOpenedCount(), HttpStatus.OK);
    }


}
