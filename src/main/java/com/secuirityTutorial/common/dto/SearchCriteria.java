package com.secuirityTutorial.common.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchCriteria {

    private String key;

    private String value;


    public SearchCriteria(String searchKey,String searchValue){
        this.key=searchKey;
        this.value=searchValue;
    }

}
