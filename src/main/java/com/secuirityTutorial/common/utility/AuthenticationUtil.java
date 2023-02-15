package com.secuirityTutorial.common.utility;

import com.secuirityTutorial.authentication.dto.UserToken;
import com.secuirityTutorial.common.exception.ServiceException;
import com.secuirityTutorial.admin.entity.Admin;
import com.secuirityTutorial.user.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class AuthenticationUtil {

	public UserToken currentLoggedInUserToken() {
		return (UserToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public User currentLoggedInUser() {

		UserToken userPrincipal = (UserToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		User user = null;

		if(userPrincipal.getUser() instanceof User){
			 user = (User) userPrincipal.getUser();
		}

		if (Objects.isNull(user)){
			throw new ServiceException("Something went wrong");
		}

		return user;

	}

	public Admin currentLoggedInAdmin() {
		UserToken userPrincipal = (UserToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Admin admin = null;

		if(userPrincipal.getUser() instanceof Admin){
			admin = (Admin) userPrincipal.getUser();
		}

		if (Objects.isNull(admin)){
			throw new ServiceException("Something went wrong");
		}

		return admin;
	}
}
