package com.secuirityTutorial.user.controller;

import com.secuirityTutorial.authentication.dto.ResponseDto;
import com.secuirityTutorial.user.dto.request.ConnectUserRequestDto;
import com.secuirityTutorial.user.dto.request.IdDto;
import com.secuirityTutorial.user.dto.request.SearchUserRequestDto;
import com.secuirityTutorial.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/welcome")
    public ResponseEntity<String> helloUser(@RequestHeader("Authorization") String Authorization){
        return new ResponseEntity<String>("Hello user", HttpStatus.OK);
    }

    @PostMapping("/get-contact-user")
    public ResponseDto getContactUser(@RequestHeader("Authorization") String Authorization, @RequestBody IdDto idDto){
        return userService.getAContactDetails(idDto);
    }

    @PostMapping("/search-users")
    public ResponseDto searchUsers(@RequestHeader("Authorization") String Authorizarion, @RequestBody SearchUserRequestDto request){
        ResponseDto responseDto = userService.searchUsers(request);
        return responseDto;
    }

    @PostMapping("/connect-user")
    public ResponseDto connectUser(@RequestHeader("Authorization") String Authorizarion, @RequestBody ConnectUserRequestDto request){
        ResponseDto responseDto = userService.connectToUser(request);
        return responseDto;
    }
}
