package com.secuirityTutorial.common.secuirity;


import com.secuirityTutorial.common.enums.Status;
import com.secuirityTutorial.admin.entity.Admin;
import com.secuirityTutorial.user.entity.User;
import com.secuirityTutorial.admin.repository.AdminRepository;
import com.secuirityTutorial.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;


@Service
public class JwtUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;


	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private Environment environment;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


		Optional<UserDetails> usersUserDetails = getUsersUserDetails(username);

		if (usersUserDetails.isEmpty()){
			return getAdminUserDetails(username).get();
		}

		return usersUserDetails.get();
	}




	private Optional<UserDetails> getAdminUserDetails(String username){

		Optional<Admin> adminOptional = adminRepository.findByEmail(username);

		Optional<UserDetails> userDetailsOptional = Optional.empty();

		if(adminOptional.isEmpty()){
			return userDetailsOptional;
		}

		boolean isActive = false;


		if(adminOptional.get().getStatus().equalsIgnoreCase(Status.ACTIVE.toString()))
		{
			isActive = true;
		}


		UserDetails userDetails = new JwtUserDetails(username, adminOptional.get().getPassword(), Arrays.asList(adminOptional.get().getRole()), isActive);

		return Optional.of(userDetails);
	}

	private Optional<UserDetails> getUsersUserDetails(String username){
		Optional<User> userOptional = userRepository.findByEmail(username);

		Optional<UserDetails> userDetailsOptional = Optional.empty();

		if(userOptional.isEmpty()){
			return userDetailsOptional;
		}

		boolean isActive = false;


		if(userOptional.get().getStatus().equalsIgnoreCase(Status.ACTIVE.toString()))
		{
			isActive = true;
		}


		UserDetails userDetails = new JwtUserDetails(username, userOptional.get().getPassword(), Arrays.asList(userOptional.get().getRole()), isActive);

		return Optional.of(userDetails);
	}

}
