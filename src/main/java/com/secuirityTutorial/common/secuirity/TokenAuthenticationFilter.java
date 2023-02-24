package com.secuirityTutorial.common.secuirity;
import com.secuirityTutorial.admin.entity.Admin;
import com.secuirityTutorial.authentication.dto.UserToken;
import com.secuirityTutorial.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
//                Long userId = tokenProvider.getUserIdFromToken(jwt);
//
//                UserDetails userDetails = customUserDetailsService.loadUserById(userId);

                String userEmail = tokenProvider.getUserEmailFromToken(jwt);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);


                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
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
