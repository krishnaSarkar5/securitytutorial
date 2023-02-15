package com.secuirityTutorial.user.specifications;

import com.secuirityTutorial.common.dto.SearchCriteria;
import com.secuirityTutorial.common.enums.Status;
import com.secuirityTutorial.common.enums.UserSearchFields;
import com.secuirityTutorial.user.entity.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<User> {


    private List<SearchCriteria> criteriaList;

    public UserSpecification(List<SearchCriteria> criteriaList){
        this.criteriaList=criteriaList;
    }


    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicateList = new ArrayList<>();

        buildPredicateList(root,criteriaBuilder,predicateList);


        return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
    }

    private void  buildPredicateList(Root<User> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList){

        predicateList.add(criteriaBuilder.equal( root.get("status"), Status.ACTIVE.toString()));

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


    private void callChargeSearch(Root<User> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SearchCriteria searchCriteria){

        predicateList.add(criteriaBuilder.equal( root.get("callCharge"),Integer.parseInt(String.valueOf(searchCriteria.getValue()))));

    }

    private void idSearch(Root<User> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SearchCriteria searchCriteria){

        predicateList.add(criteriaBuilder.equal( root.get("id"),Long.parseLong(String.valueOf(searchCriteria.getValue()))));

    }

    private void phoneSearch(Root<User> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SearchCriteria searchCriteria){

        predicateList.add(criteriaBuilder.like( root.get("phoneNumber").get("phoneNumber"),"%"+String.valueOf(searchCriteria.getValue())+"%"));

    }

    private void emailSearch(Root<User> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SearchCriteria searchCriteria){

        predicateList.add(criteriaBuilder.like( root.get("email"),"%"+String.valueOf(searchCriteria.getValue())+"%"));

    }

    private void addressSearch(Root<User> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SearchCriteria searchCriteria){

        predicateList.add(criteriaBuilder.like( root.get("address"),"%"+String.valueOf(searchCriteria.getValue())+"%"));

    }

    private void firstNameSearch(Root<User> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SearchCriteria searchCriteria){

        predicateList.add(criteriaBuilder.like( root.get("firstName"),"%"+String.valueOf(searchCriteria.getValue())+"%"));

    }
    private void lastNameSearch(Root<User> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SearchCriteria searchCriteria){

        predicateList.add(criteriaBuilder.like( root.get("lastName"),"%"+String.valueOf(searchCriteria.getValue())+"%"));

    }
}
