package com.secuirityTutorial.user.scheduler;

import com.secuirityTutorial.common.utility.EmailSender;
import com.secuirityTutorial.user.entity.User;
import com.secuirityTutorial.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BirthdayMailSender {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSender emailSender;

    @Scheduled(cron = "0 0 0 * * *")
    public void sendBirthDayMail(){


        List<User> allUsers = getAllUserHaveBirthdayToday();

        sendMail(allUsers);

    }


    private  List<User> getAllUserHaveBirthdayToday(){
        return userRepository.findAllByDob(LocalDate.now());
    }

    private void sendMail(List<User> allUsers){

        for (User user : allUsers){
            boolean isMailSend = emailSender.sendMail(user.getEmail(), "Happy Birthday", "Many Many Happy Returns of the day " + user.getFirstName() + " " + user.getLastName());

            if(isMailSend){
                System.out.println("Mail successfully send to "+  user.getFirstName() + " " + user.getLastName());
            }

        }
    }

}
