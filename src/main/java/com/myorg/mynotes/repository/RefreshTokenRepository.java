package com.myorg.mynotes.repository;


import com.myorg.mynotes.entity.RefreshToken;
import com.myorg.mynotes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    Optional<RefreshToken> findByUser(User user);

    void deleteByUser(User user);

    @Modifying
    @Query("delete from RefreshToken r where r.user.id = :userId")
    void deleteByUser_Id(Long userId);

    RefreshToken findByUser_Id(Long userId);

}