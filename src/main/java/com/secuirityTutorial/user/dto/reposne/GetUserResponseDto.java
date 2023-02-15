package com.secuirityTutorial.user.dto.reposne;

import com.secuirityTutorial.user.entity.User;
import lombok.Data;

import java.time.format.DateTimeFormatter;
@Data
public class GetUserResponseDto {

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNo;

    private String dob;

    private Integer callCharge;

    private String address;

    public GetUserResponseDto(User user){
        this.id=user.getId();
        this.firstName= user.getFirstName();
        this.lastName= user.getLastName();
        this.email=user.getEmail();
        this.phoneNo=user.getPhoneNumber().getPhoneNumber();
        this.dob=user.getDob().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.callCharge=user.getCallCharge();
        this.address= user.getAddress();
    }
}
