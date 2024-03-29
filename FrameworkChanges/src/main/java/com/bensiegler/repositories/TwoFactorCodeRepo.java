package com.bensiegler.repositories;

import com.bensiegler.models.TwoFactorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TwoFactorCodeRepo extends JpaRepository<TwoFactorCode, Long> {
    TwoFactorCode findByUserId(Long userId);
    void deleteById(Long id);
    Optional<TwoFactorCode> findByCookie(String cookie);
}
