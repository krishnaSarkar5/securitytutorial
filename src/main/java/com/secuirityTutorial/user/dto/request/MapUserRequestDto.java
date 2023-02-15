package com.secuirityTutorial.user.dto.request;

import com.secuirityTutorial.common.exception.ServiceException;
import lombok.Data;

import java.util.*;

@Data
public class MapUserRequestDto {

    private Long userId;

    private List<Long> linkedUsersId;

    public void validate(){

        Map<String,String> errorMap = new HashMap<>();

        if(Objects.isNull(userId) || userId==0L){
            errorMap.put("user Id","User id cant be empty");
        }

        if (linkedUsersId.isEmpty()){
            errorMap.put("Linked User id list","Linked User id list can no be empty");
        } else {
            Set<Long> uniqueIdList = new HashSet(linkedUsersId);
            if (uniqueIdList.size()!=linkedUsersId.size()){
                errorMap.put("Linked User id list","Repeated id value present in Linked User id list");
            }
        }

        if (errorMap.size()>0){
            throw new ServiceException("Invalid Request",errorMap);
        }
    }
}
