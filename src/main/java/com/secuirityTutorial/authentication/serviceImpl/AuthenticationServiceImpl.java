package com.secuirityTutorial.authentication.serviceImpl;


import com.secuirityTutorial.authentication.dto.LoginDto;
import com.secuirityTutorial.authentication.dto.ResponseDto;
import com.secuirityTutorial.authentication.service.AuthenticationService;
import com.secuirityTutorial.common.enums.Status;
import com.secuirityTutorial.common.exception.ServiceException;
import com.secuirityTutorial.common.secuirity.JwtUserDetailService;
import com.secuirityTutorial.common.secuirity.JwtUtils;
import com.secuirityTutorial.common.utility.HashStringGenerator;
import com.secuirityTutorial.admin.entity.Admin;
import com.secuirityTutorial.admin.entity.AdminLoginToken;
import com.secuirityTutorial.user.entity.User;
import com.secuirityTutorial.user.entity.UserLoginToken;
import com.secuirityTutorial.admin.repository.AdminLoginTokenRepository;
import com.secuirityTutorial.admin.repository.AdminRepository;
import com.secuirityTutorial.user.repository.UserLoginTokenRepository;
import com.secuirityTutorial.user.repository.UserRepository;
import com.secuirityTutorial.common.utility.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {


    @Autowired
    private JwtUserDetailService jwtUserDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private Environment environment;

    @Autowired
    private UserLoginTokenRepository userLoginTokenRepository;
    
    
    @Autowired
    private AdminLoginTokenRepository adminLoginTokenRepository;

//    @Autowired
//    private CommonUtils commonUtils;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationUtil authenticationUtil;

    @Autowired
    private HashStringGenerator hashStringGenerator;
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public ResponseDto loginWithPassword(LoginDto loginDto) {
        validateIncomingRequest(loginDto);

        Optional<ResponseDto> responseDtoForUserOptional = userLogin(loginDto);

        if(responseDtoForUserOptional.isEmpty()){
            return adminLogin(loginDto).get();
        }

        return responseDtoForUserOptional.get();
    }

    private Optional<ResponseDto> userLogin(LoginDto loginDto) {
        Optional<User> existedUserOptional = userRepository.findByEmail(loginDto.getUsername());

        if(existedUserOptional.isEmpty()) {
            return Optional.empty();
        }

        User existedUser = existedUserOptional.get();

        if(!existedUser.getStatus().equalsIgnoreCase(Status.ACTIVE.toString())) {
            throw new ServiceException("User Disabled");
        }

        return Optional.of(this.getJwtTokenForUser(existedUser, loginDto));
    }

    private ResponseDto getJwtTokenForUser(User existedUser, LoginDto loginDto) {

        UserDetails userDetails = jwtUserDetailService.loadUserByUsername(existedUser.getEmail());


        authenticateUser(loginDto, userDetails);


        String token = generateToken(existedUser, userDetails);

        saveUserLoginToken(existedUser, token);


        Map<String ,String > responeMap = new HashMap<>();
        responeMap.put("token",token);

        ResponseDto responseDto=new ResponseDto();
        responseDto.setStatus(true);
        responseDto.setMessage(environment.getProperty("successResponse"));
        responseDto.setData(responeMap);
        return responseDto;
    }

    private String generateToken(User existedUser, UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("FirstName", existedUser.getFirstName());
        claims.put("LastName", existedUser.getLastName());

        claims.put("role", existedUser.getRole());
        claims.put("status", existedUser.getStatus());


        return "Bearer "+jwtUtils.generateToken(userDetails, claims);
    }

    private void saveUserLoginToken(User existedUser, String token) {
        UserLoginToken userLoginToken = new UserLoginToken();
        userLoginToken.setToken(token);
        userLoginToken.setLoginTime(LocalDateTime.now());
        userLoginToken.setCreatedAt(LocalDateTime.now());
        userLoginToken.setUpdatedAt(LocalDateTime.now());
        userLoginToken.setUser(existedUser);
        userLoginToken.setStatus(Status.ACTIVE.toString());
        userLoginTokenRepository.save(userLoginToken);
    }

    @Override
    public ResponseDto logout(String token) {
        ResponseDto responseDto = new ResponseDto();

        try {
            userLogut(token);
        }catch (Exception e){

           adminLogout(token);
        }


        responseDto.setStatus(true);
        responseDto.setMessage("SUCCESSFULL");
        responseDto.setData("Log out successfully");

        return responseDto;
    }

    private void userLogut(String token) {
        UserLoginToken existedToken = userLoginTokenRepository.findByTokenAndStatus(token, Status.ACTIVE.toString());

        if(Objects.isNull(existedToken)) {
            throw new ServiceException("INVALID_DATA");
        }

        existedToken.setStatus(Status.INACTIVE.toString());
        existedToken.setLogoutTime(LocalDateTime.now());

        userLoginTokenRepository.save(existedToken);
    }

    private void adminLogout(String token) {
        AdminLoginToken existedToken = adminLoginTokenRepository.findByTokenAndStatus(token, Status.ACTIVE.toString());

        if(Objects.isNull(existedToken)) {
            throw new ServiceException("INVALID_DATA");
        }

        existedToken.setStatus(Status.INACTIVE.toString());
        existedToken.setLogoutTime(LocalDateTime.now());

        adminLoginTokenRepository.save(existedToken);
    }

    private void validateIncomingRequest(Object object) {

       if(object instanceof LoginDto){
           LoginDto request = (LoginDto) object;
           request.validate();
       }else if(object instanceof String){
           String request = (String) object;
           if(Objects.isNull(request) || request.trim().equalsIgnoreCase(""))
                    throw new ServiceException("Process error");
       }
       else {
           throw new ServiceException("Process error");
       }

    }


    private Optional<ResponseDto> adminLogin(LoginDto loginDto) {
        Optional<Admin> existedAdminOptional = adminRepository.findByEmail(loginDto.getUsername());

        if(existedAdminOptional.isEmpty()) {
            return Optional.empty();
        }

        Admin existedAdmin = existedAdminOptional.get();

        if(!existedAdmin.getStatus().equalsIgnoreCase(Status.ACTIVE.toString())) {
            throw new ServiceException("User Disabled");
        }

        return Optional.of(this.getJwtTokenForAdmin(existedAdmin, loginDto));
    }

    private ResponseDto getJwtTokenForAdmin(Admin existedAdmin, LoginDto loginDto) {

        UserDetails userDetails = jwtUserDetailService.loadUserByUsername(existedAdmin.getEmail());


        authenticateUser(loginDto, userDetails);


        String token = generateToken(existedAdmin, userDetails);

        saveAdminLoginToken(existedAdmin, token);


        Map<String ,String > responeMap = new HashMap<>();
        responeMap.put("token",token);

        ResponseDto responseDto=new ResponseDto();
        responseDto.setStatus(true);
        responseDto.setMessage(environment.getProperty("successResponse"));
        responseDto.setData(responeMap);
        return responseDto;
    }



    private String generateToken(Admin existedAdmin, UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("FirstName", existedAdmin.getFirstName());
        claims.put("LastName", existedAdmin.getLastName());

        claims.put("role", existedAdmin.getRole());
        claims.put("status", existedAdmin.getStatus());


        return "Bearer "+jwtUtils.generateToken(userDetails, claims);
    }
    private void authenticateUser(LoginDto loginDto, UserDetails userDetails) {
        if(!Objects.isNull(loginDto)) {
            try
            {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails, hashStringGenerator.reGenerateStorngPasswordHash(loginDto.getPassword(),userDetails.getPassword())));
//				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails, password));
            }
            catch (DisabledException e)
            {
                throw new ServiceException("USER_DISABLED", HttpStatus.UNAUTHORIZED);
            }
            catch (BadCredentialsException e)
            {
                throw new ServiceException("INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                throw new ServiceException("Please put valid credentials!", HttpStatus.UNAUTHORIZED);
            }



        }
    }



    private void saveAdminLoginToken(Admin existedUser, String token) {
        AdminLoginToken adminLoginToken = new AdminLoginToken();
        adminLoginToken.setToken(token);
        adminLoginToken.setLoginTime(LocalDateTime.now());
        adminLoginToken.setCreatedAt(LocalDateTime.now());
        adminLoginToken.setUpdatedAt(LocalDateTime.now());
        adminLoginToken.setAdmin(existedUser);
        adminLoginToken.setStatus(Status.ACTIVE.toString());
        adminLoginTokenRepository.save(adminLoginToken);
    }
}
