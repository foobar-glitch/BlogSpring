package com.example.blog.mariadb.cookieTable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CookieTableService {
    @Autowired
    private CookieTableRepository cookieTableRepository;

    public Optional<CookieTable> findByCookieData(String cookieData){
        return cookieTableRepository.findByCookieData(cookieData);
    }

    public CookieTable save(CookieTable cookieTable) {
        return cookieTableRepository.save(cookieTable);
    }

    public Optional<CookieTable> findById(Long cookieId) {
        return cookieTableRepository.findById(cookieId);
    }

    public void deleteById(Long cookieId) {
        cookieTableRepository.deleteById(cookieId);
    }

}
