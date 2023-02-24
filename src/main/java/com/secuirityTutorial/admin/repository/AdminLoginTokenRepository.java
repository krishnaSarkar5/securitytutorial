package com.secuirityTutorial.admin.repository;

import com.secuirityTutorial.admin.entity.AdminLoginToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminLoginTokenRepository extends JpaRepository<AdminLoginToken,Long> {

    AdminLoginToken findByAdmin_emailAndTokenAndStatus(String email, String requestTokenHeader, String i);

    AdminLoginToken findByTokenAndStatus(String requestTokenHeader, String i);
}
