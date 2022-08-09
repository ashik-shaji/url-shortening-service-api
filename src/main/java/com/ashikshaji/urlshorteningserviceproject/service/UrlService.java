package com.ashikshaji.urlshorteningserviceproject.service;

import com.ashikshaji.urlshorteningserviceproject.model.Url;
import com.ashikshaji.urlshorteningserviceproject.model.UrlDto;
import org.springframework.stereotype.Service;

@Service
public interface UrlService {
    public Url generateShortLink(UrlDto urlDto);
    public Url persistShortLink(Url url);
    public Url getEncodedUrl(String url);
    public void deleteShortLink(Url url);
    public void incrementOpenedCount(String shortLink);
}
