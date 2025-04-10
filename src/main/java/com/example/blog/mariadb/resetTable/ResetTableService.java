package com.example.blog.mariadb.resetTable;

import com.example.blog.mariadb.cookieTable.CookieTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResetTableService {
    @Autowired
    private ResetTableRepository resetTableRepository;

    public Optional<ResetTable> findByResetToken(String resetToken) {
        return resetTableRepository.findByResetToken(resetToken);
    }

    public ResetTable save(ResetTable resetTable) {
        return resetTableRepository.save(resetTable);
    }

    public void deleteById(Long resetTableId) {
        resetTableRepository.deleteById(resetTableId);
    }


}
