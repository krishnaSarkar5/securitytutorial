package com.secuirityTutorial.admin.controller;

import com.secuirityTutorial.authentication.dto.ResponseDto;
import com.secuirityTutorial.user.dto.request.MapUserRequestDto;
import com.secuirityTutorial.user.dto.request.SearchUserRequestDto;
import com.secuirityTutorial.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/welcome")
    public ResponseEntity<String> helloAdmin(@RequestHeader("Authorization") String Authorizarion){
        return new ResponseEntity<String>("Hello Admin", HttpStatus.OK);
    }

    @PostMapping("/link-users")
    public ResponseDto linkUsers(@RequestHeader("Authorization") String Authorizarion, @RequestBody MapUserRequestDto request){
        ResponseDto responseDto = adminService.mapUsers(request);
        return responseDto;
    }

    @PostMapping("/search-users")
    public ResponseDto searchUsers(@RequestHeader("Authorization") String Authorizarion, @RequestBody SearchUserRequestDto request){
        ResponseDto responseDto = adminService.searchUsers(request);
        return responseDto;
    }
}
