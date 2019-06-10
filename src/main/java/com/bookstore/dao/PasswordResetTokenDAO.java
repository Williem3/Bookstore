package com.bookstore.dao;


import com.bookstore.entity.User;
import com.bookstore.entity.security.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.stream.Stream;

public interface PasswordResetTokenDAO extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);

    Stream<PasswordResetToken> findAllByExpirationDateLessThan(Date now);

    @Modifying
    @Query("delete from PasswordResetToken t where t.expirationDate <= ?1")
    void deleteAllExpiredSince(Date now);
}
