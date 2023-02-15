package com.secuirityTutorial.user.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.secuirityTutorial.common.enums.SortType;
import com.secuirityTutorial.common.enums.UserSearchFields;
import com.secuirityTutorial.common.enums.UserSearchSortByField;
import com.secuirityTutorial.common.exception.ServiceException;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Data
public class SearchUserRequestDto {

    private List<String> searchKey;

    private List<String> searchValue;

    private int pageSize;

    private int offset;

    private String sortBy;

    // ASC or DESC
    private String sortType;

    @JsonIgnore
    public void validate(){

        Map<String ,String> errorMap = new HashMap<>();

        if(!searchKey.isEmpty() && !searchValue.isEmpty() && searchKey.size()!=searchValue.size()){
            errorMap.put("Invalid search key value","Size of list of search key and are not same");
        }

        validateSearchKeys(errorMap);

        if(!Objects.isNull(sortBy) && sortBy.equalsIgnoreCase("")){

            errorMap.put("sortBy","Invalid sort by value");

        }else if(!Objects.isNull(sortBy) && Objects.isNull(UserSearchSortByField.valueOf(sortBy.toUpperCase()))){
            errorMap.put(sortBy,"Invalid sort by value");
        }

        if(!Objects.isNull(sortType) && sortType.equalsIgnoreCase("") ){
            errorMap.put(sortType,"Invalid sort by value");
        } else if (!Objects.isNull(sortType) && Objects.isNull(SortType.valueOf(sortType.toUpperCase()))) {
            errorMap.put(sortType,"Invalid sort by value");
        }

        if (errorMap.size()>0){
            throw new ServiceException("Invalid Request",errorMap);
        }

    }
    @JsonIgnore
    private void validateSearchKeys(Map<String, String> errorMap) {
        if(!searchKey.isEmpty() && !searchValue.isEmpty()){

            for (String seacrhField : searchKey){
               if( Objects.isNull(UserSearchFields.valueOf(seacrhField.toUpperCase()))){
                   errorMap.put(seacrhField,"Invalid SearchKey");
               }
            }


        }
    }

    @JsonIgnore
    public boolean isSearchKeyPresent(){
        return !searchKey.isEmpty();
    }

}
