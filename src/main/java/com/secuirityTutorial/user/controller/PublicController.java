package com.secuirityTutorial.user.controller;

import com.secuirityTutorial.user.scheduler.BirthdayMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {


    @Autowired
    private BirthdayMailSender birthdayMailSender;


    @GetMapping("/welcome")
    public ResponseEntity<String> helloWorld(){
        return new ResponseEntity<String>("Hello World", HttpStatus.OK);
    }



    @GetMapping("/test")
    public void birthdaymailsendertest(){
        birthdayMailSender.sendBirthDayMail();
    }


}
