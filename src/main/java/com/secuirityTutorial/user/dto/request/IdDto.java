package com.secuirityTutorial.user.dto.request;

import com.secuirityTutorial.common.exception.ServiceException;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class IdDto {

    private Long id;

    public void validate(){

        Map<String,String> errorMap = new HashMap<>();

        if(Objects.isNull(id) || id==0l){
            errorMap.put("Id","Invalid id");
        }
        if(errorMap.size()>0){
            throw new ServiceException("Invalid Request",errorMap);
        }
    }
}
