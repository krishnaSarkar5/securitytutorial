package com.secuirityTutorial.common.secuirity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;




public class JwtUserDetails {
	
//
//
//	private String username;
//
//	private String password;
//
//	private boolean isActive;
//	private List<String> listAuthority;
//
//
//	public JwtUserDetails(String username, String password, List<String> listAuthority, boolean isActive) {
//		super();
//		this.username = username;
//		this.password = password;
//		this.listAuthority = listAuthority;
//		this.isActive = isActive;
//	}
//
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return listAuthority.stream()
//				.map(role -> new SimpleGrantedAuthority(role))
//				.collect(Collectors.toList());
//	}
//
//	@Override
//	public String getPassword() {
//		// TODO Auto-generated method stub
//		return password;
//	}
//
//	@Override
//	public String getUsername() {
//		// TODO Auto-generated method stub
//		return username;
//	}
//
//	@Override
//	public boolean isAccountNonExpired() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@Override
//	public boolean isEnabled() {
//		// TODO Auto-generated method stub
//		return isActive;
//	}

}
