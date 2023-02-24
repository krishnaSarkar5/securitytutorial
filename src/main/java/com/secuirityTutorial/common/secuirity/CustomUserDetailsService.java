package com.secuirityTutorial.common.secuirity;

import com.secuirityTutorial.admin.entity.Admin;
import com.secuirityTutorial.admin.repository.AdminRepository;
import com.secuirityTutorial.common.enums.Status;
import com.secuirityTutorial.common.exception.ResourceNotFoundException;
import com.secuirityTutorial.common.exception.ServiceException;
import com.secuirityTutorial.user.entity.User;
import com.secuirityTutorial.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdminRepository adminRepository;



    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isPresent() && userOptional.get().getStatus().equals(Status.ACTIVE.toString())){
            UserPrincipal userPrincipal = UserPrincipal.create(userOptional.get());

            return UserPrincipal.create(userOptional.get());
        }else {
            Admin admin = adminRepository.findByEmail(email).orElseThrow(() -> new ServiceException("No user with found with username : " + email));

            if (admin.getStatus().equals(Status.ACTIVE.toString())){
                return UserPrincipal.create(admin);
            }

            throw new ServiceException("No user with found with username : " + email);
        }


    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }
}
