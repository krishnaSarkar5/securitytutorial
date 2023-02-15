package com.secuirityTutorial.user.specifications;

import com.secuirityTutorial.common.dto.SearchCriteria;
import com.secuirityTutorial.common.enums.Status;
import com.secuirityTutorial.common.enums.UserSearchFields;
import com.secuirityTutorial.user.entity.User;
import com.secuirityTutorial.user.entity.UserMappingDetails;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserMappingDetailsSpecification implements Specification<UserMappingDetails> {

    List<SearchCriteria> criteriaList;

    User loggeInUser;

    public UserMappingDetailsSpecification(List<SearchCriteria> criteriaList, User loggeInUser) {
        this.criteriaList = criteriaList;
        this.loggeInUser=loggeInUser;
    }

    @Override
    public Predicate toPredicate(Root<UserMappingDetails> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicateList = new ArrayList<>();

        buildPredicateList(root,criteriaBuilder,predicateList);


        return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
    }



    private void  buildPredicateList(Root<UserMappingDetails> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList){

        predicateList.add(criteriaBuilder.equal( root.get("user").get("id"), loggeInUser.getId()));

        for (SearchCriteria criteria : criteriaList){

            UserSearchFields searchKey = UserSearchFields.valueOf(criteria.getKey().toUpperCase());

            switch (searchKey){

                case CALL_CHARGE:{
                    callChargeSearch(root,criteriaBuilder,predicateList,criteria);
                    break;
                }

                case EMAIL:{
                    emailSearch(root,criteriaBuilder,predicateList,criteria);
                    break;
                }

                case PHONE:{
                    phoneSearch(root,criteriaBuilder,predicateList,criteria);
                    break;
                }
                case ADDRESS:{
                    addressSearch(root,criteriaBuilder,predicateList,criteria);
                    break;
                }
                case ID:{
                    idSearch(root,criteriaBuilder,predicateList,criteria);
                    break;
                }
                case FIRSTNAME:{
                    firstNameSearch(root,criteriaBuilder,predicateList,criteria);
                    break;
                }
                case LASTNAME:{
                    lastNameSearch(root,criteriaBuilder,predicateList,criteria);
                    break;
                }
            }

        }

    }


    private void callChargeSearch(Root<UserMappingDetails> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SearchCriteria searchCriteria){

        predicateList.add(criteriaBuilder.equal( root.get("linkedTo").get("callCharge"),Integer.parseInt(String.valueOf(searchCriteria.getValue()))));

    }

    private void idSearch(Root<UserMappingDetails> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SearchCriteria searchCriteria){

        predicateList.add(criteriaBuilder.equal( root.get("linkedTo").get("id"),Long.parseLong(String.valueOf(searchCriteria.getValue()))));

    }

    private void phoneSearch(Root<UserMappingDetails> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SearchCriteria searchCriteria){

        predicateList.add(criteriaBuilder.like( root.get("linkedTo").get("phoneNumber").get("phoneNumber"),"%"+String.valueOf(searchCriteria.getValue())+"%"));

    }

    private void emailSearch(Root<UserMappingDetails> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SearchCriteria searchCriteria){

        predicateList.add(criteriaBuilder.like( root.get("linkedTo").get("email"),"%"+String.valueOf(searchCriteria.getValue())+"%"));

    }

    private void addressSearch(Root<UserMappingDetails> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SearchCriteria searchCriteria){

        predicateList.add(criteriaBuilder.like( root.get("linkedTo").get("address"),"%"+String.valueOf(searchCriteria.getValue())+"%"));

    }

    private void firstNameSearch(Root<UserMappingDetails> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SearchCriteria searchCriteria){

        predicateList.add(criteriaBuilder.like( root.get("linkedTo").get("firstName"),"%"+String.valueOf(searchCriteria.getValue())+"%"));

    }
    private void lastNameSearch(Root<UserMappingDetails> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SearchCriteria searchCriteria){

        predicateList.add(criteriaBuilder.like( root.get("linkedTo").get("lastName"),"%"+String.valueOf(searchCriteria.getValue())+"%"));

    }
}
