package com.secuirityTutorial.user.serviceImpl;

import com.secuirityTutorial.authentication.dto.ResponseDto;
import com.secuirityTutorial.common.enums.ResponseStatus;
import com.secuirityTutorial.common.enums.Role;
import com.secuirityTutorial.common.exception.ServiceException;
import com.secuirityTutorial.common.utility.HashStringGenerator;
import com.secuirityTutorial.user.dto.request.RegistrationRequestDto;
import com.secuirityTutorial.admin.entity.Admin;
import com.secuirityTutorial.user.entity.User;
import com.secuirityTutorial.admin.repository.AdminRepository;
import com.secuirityTutorial.user.repository.UserRepository;
import com.secuirityTutorial.user.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HashStringGenerator hashStringGenerator;
    @Autowired
    private AdminRepository adminRepository;


    @Override
    public ResponseDto registerUser(RegistrationRequestDto request) {


            validateIncomingRequest(request);

            Role role = Role.valueOf(request.getRole().toUpperCase());

            switch (role){
                case USER:{
                    saveUser(request);
                    break;
                }
                case ADMIN:{
                    saveAdmin(request);
                    break;
                }
            }

            return getSuccessResponseDto("Registered Successfully");



    }

    private void saveUser(RegistrationRequestDto request) {
        User newUser = new User(request);

        hashUserPassword(newUser);

        userRepository.save(newUser);
    }

    private void saveAdmin(RegistrationRequestDto request){

        Admin admin = new Admin(request);
        hashUserPassword(admin);
        adminRepository.save(admin);
    }

    private void hashUserPassword(User user){
        user.setPassword(hashStringGenerator.generateStorngPasswordHash(user.getPassword()));
    }

    private void hashUserPassword(Admin admin){
        admin.setPassword(hashStringGenerator.generateStorngPasswordHash(admin.getPassword()));
    }
    public ResponseDto getSuccessResponseDto(Object data){

        ResponseDto responseDto = new ResponseDto();

        responseDto.setMessage(ResponseStatus.SUCCESS.toString());
        responseDto.setStatus(true);
        responseDto.setData(data);
        return responseDto;
    }

    public ResponseDto getFailureResponseDto(Object data){

        ResponseDto responseDto = new ResponseDto();

        responseDto.setMessage(ResponseStatus.FAILURE.toString());
        responseDto.setStatus(false);
        responseDto.setData(data);
        return responseDto;
    }

    private void validateIncomingRequest(Object object){

        if(object instanceof RegistrationRequestDto){
            RegistrationRequestDto request = (RegistrationRequestDto) object;
            request.validate();
        }else {
            throw new ServiceException("Process Error");
        }
    }
}
