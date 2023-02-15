package com.secuirityTutorial.common.secuirity;


import com.secuirityTutorial.authentication.dto.UserToken;
import com.secuirityTutorial.common.enums.Status;
import com.secuirityTutorial.admin.entity.Admin;
import com.secuirityTutorial.admin.entity.AdminLoginToken;
import com.secuirityTutorial.user.entity.User;
import com.secuirityTutorial.user.entity.UserLoginToken;
import com.secuirityTutorial.admin.repository.AdminLoginTokenRepository;
import com.secuirityTutorial.admin.repository.AdminRepository;
import com.secuirityTutorial.user.repository.UserLoginTokenRepository;
import com.secuirityTutorial.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUserDetailService jwtUserDetailService;
	
	@Autowired
	private JwtUtils jwtUtils; 
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserLoginTokenRepository userLoginTokenRepository;
	
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private AdminLoginTokenRepository adminLoginTokenRepository;
	
	@Autowired
	private Environment environment;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String requestTokenHeader = request.getHeader("Authorization");
//		System.out.println(request.getRequestURI());
		String email = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		logger.info(" incoming token = "+requestTokenHeader);
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				email = jwtUtils.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
	
		// Once we get the token validate it.
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			
			// this will come from respective service

			Map<String, Object> responseMap = isTokenActiveAndUserActive(email, jwtToken);

			boolean isTokenActiveAndUserActiveBool = Boolean.parseBoolean(responseMap.get("isTokenActiveAndUserActive").toString());

			Object principle = responseMap.get("principle");

			UserDetails userDetails = (UserDetails) responseMap.get("userDetails");

			if (isTokenActiveAndUserActiveBool)
			{
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						principle, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		System.out.println("out of filter");
		filterChain.doFilter(request, response);
	}


	private Map<String,Object> isTokenActiveAndUserActive(String email, String token){
		Optional<Map<String, Object>> tokenActiveAndUserActiveForUser = isTokenActiveAndUserActiveForUser(email, token);

		if (tokenActiveAndUserActiveForUser.isEmpty()){
			return isTokenActiveAndUserActiveForAdmin(email,token).get();
		}
		return tokenActiveAndUserActiveForUser.get();
	}

	private Optional<Map<String,Object>> isTokenActiveAndUserActiveForUser(String email, String token){


		UserDetails userDetails = this.jwtUserDetailService.loadUserByUsername(email);


		//validates if the username is phone number or email address
		Optional<User> userOptional = userRepository.findByEmail(email);

		if (userOptional.isEmpty()){
			return Optional.empty();
		}

		Map<String,Object> responseMap = new HashMap<>();

		UserLoginToken userLoginToken = userLoginTokenRepository.findByUser_idAndTokenAndStatus(userOptional.get().getId(), "Bearer "+token, Status.ACTIVE.toString());
		boolean isTokenActive = !Objects.isNull(userLoginToken);




		boolean isTokenValidForActiveUsers = jwtUtils.validateTokenWExpirationValidation(token, userDetails)
				&& userOptional.get().getStatus().equalsIgnoreCase(Status.ACTIVE.toString());

		UserToken principleForUser = getPrinciple(userOptional.get());

		responseMap.put("isTokenActiveAndUserActive",isTokenActive && isTokenValidForActiveUsers);
		responseMap.put("principle",principleForUser);
		responseMap.put("userDetails",userDetails);

		return Optional.of(responseMap);
	}

	private Optional<Map<String,Object>> isTokenActiveAndUserActiveForAdmin(String email, String token){


		UserDetails userDetails = this.jwtUserDetailService.loadUserByUsername(email);


		//validates if the username is phone number or email address
		Optional<Admin> adminOptional = adminRepository.findByEmail(email);

		if (adminOptional.isEmpty()){
			return Optional.empty();
		}

		Map<String,Object> responseMap = new HashMap<>();

		AdminLoginToken adminLoginToken = adminLoginTokenRepository.findByAdmin_idAndTokenAndStatus(adminOptional.get().getId(), "Bearer "+token, Status.ACTIVE.toString());
		boolean isTokenActive = !Objects.isNull(adminLoginToken);




		boolean isTokenValidForActiveUsers = jwtUtils.validateTokenWExpirationValidation(token, userDetails)
				&& adminOptional.get().getStatus().equalsIgnoreCase(Status.ACTIVE.toString());

		UserToken principle = getPrinciple(adminOptional.get());

		responseMap.put("isTokenActiveAndUserActive",isTokenActive && isTokenValidForActiveUsers);
		responseMap.put("principle",principle);
		responseMap.put("userDetails",userDetails);

		return Optional.of(responseMap);
	}

	private UserToken getPrinciple(User user){

		UserToken principal=new UserToken();
		principal.setId(user.getId());
		principal.setStatus(user.getStatus());
		principal.setUsername(user.getEmail());

		principal.setUser(user);

		return principal;
	}

	private UserToken getPrinciple(Admin admin){

		UserToken principal=new UserToken();
		principal.setId(admin.getId());
		principal.setStatus(admin.getStatus());
		principal.setUsername(admin.getEmail());

		principal.setUser(admin);

		return principal;
	}


}
