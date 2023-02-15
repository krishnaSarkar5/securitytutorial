package com.secuirityTutorial.user.repository;


import com.secuirityTutorial.user.entity.UserLoginToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserLoginTokenRepository extends JpaRepository<UserLoginToken, Long> {

	UserLoginToken findByUser_idAndTokenAndStatus(Long id, String requestTokenHeader, String i);
	
	UserLoginToken findByTokenAndStatus(String requestTokenHeader, String i);

}
