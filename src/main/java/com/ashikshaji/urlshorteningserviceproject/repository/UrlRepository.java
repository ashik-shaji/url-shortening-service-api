package com.ashikshaji.urlshorteningserviceproject.repository;

import com.ashikshaji.urlshorteningserviceproject.model.Url;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository {
    public Url findByShortLink(String shortLink);

    Url save(Url url);

    void delete(Url url);
}
