package com.secuirityTutorial.user.dto.request;

import com.secuirityTutorial.common.exception.ServiceException;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class ConnectUserRequestDto {

    private Long userId;

    private String transactionId;

    public void validate(){

        Map<String ,String> erroMap = new HashMap<>();


        if(Objects.isNull(userId) || userId==0l){
            erroMap.put("user id","Invalid user Id");
        }

        if(Objects.isNull(transactionId) || transactionId.equalsIgnoreCase("")){
            erroMap.put("transaction id","Invalid user transaction Id");
        }

        if (erroMap.size()>0){
            throw new ServiceException("Invalid Request",erroMap);
        }
    }

}
