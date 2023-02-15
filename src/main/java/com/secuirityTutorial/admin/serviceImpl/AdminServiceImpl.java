package com.secuirityTutorial.admin.serviceImpl;

import com.secuirityTutorial.admin.entity.Admin;
import com.secuirityTutorial.admin.service.AdminService;
import com.secuirityTutorial.authentication.dto.ResponseDto;
import com.secuirityTutorial.common.dto.Response;
import com.secuirityTutorial.common.dto.SearchCriteria;
import com.secuirityTutorial.common.enums.ResponseStatus;
import com.secuirityTutorial.common.enums.Status;
import com.secuirityTutorial.common.exception.ServiceException;
import com.secuirityTutorial.common.utility.AuthenticationUtil;
import com.secuirityTutorial.user.dto.request.MapUserRequestDto;
import com.secuirityTutorial.user.dto.reposne.GetUserResponseDto;
import com.secuirityTutorial.user.dto.request.SearchUserRequestDto;
import com.secuirityTutorial.user.entity.User;
import com.secuirityTutorial.user.entity.UserMappingDetails;
import com.secuirityTutorial.user.repository.UserMappingDetailsRepository;
import com.secuirityTutorial.user.repository.UserRepository;
import com.secuirityTutorial.user.specifications.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMappingDetailsRepository userMappingDetailsRepository;
    
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private AuthenticationUtil authenticationUtil;

    @Override
    public ResponseDto mapUsers(MapUserRequestDto request) {

            validateIncomingRequest(request);

            checkIsMappingExist(request);

            List<UserMappingDetails> userMappingDetails = generateNewMappingData(request);

            saveUserMappingDetails(userMappingDetails);

            return getSuccessResponseDto("Users successfully linked");

    }

    @Override
    public ResponseDto searchUsers(SearchUserRequestDto request) {

        try {
            validateIncomingRequest(request);

            List<User> allUsers = getAllUsers(request);

            List<GetUserResponseDto> userResponseDtoList = getUserResponseDtoList(allUsers);

            Integer resultCount = getTotalCountBySpecification(request);

            Map<String,Object> dataMap = new HashMap<>();

            dataMap.put("count",resultCount);
            dataMap.put("searchResult",userResponseDtoList);

            return new Response().getSuccessResponseDto(dataMap);
        }catch (Exception e){
            e.printStackTrace();

            return new Response().getFailureResponseDto("Something went wrong");
        }

    }



























    private void validateIncomingRequest(Object object){
       try {
           if(object instanceof MapUserRequestDto){
               MapUserRequestDto request = (MapUserRequestDto) object;
               request.validate();
           }
           else if(object instanceof SearchUserRequestDto){
               SearchUserRequestDto request = (SearchUserRequestDto) object;
               request.validate();
           }
       }catch (Exception e){
           e.printStackTrace();
       }

//        else {
//            throw new ServiceException("Something went wrong");
//        }
    }

    private void checkIsMappingExist(MapUserRequestDto request){

        List<UserMappingDetails> existedMappingDetails = userMappingDetailsRepository.findByUserIdAndLinkedToIdIn(request.getUserId(), request.getLinkedUsersId());

        if(!existedMappingDetails.isEmpty()){
            StringBuffer sb = new StringBuffer();
            for(UserMappingDetails existedMapping : existedMappingDetails){
                sb.append(existedMapping.getUser().getId()+"-->"+existedMapping.getLinkedTo().getId()+" , ");
            }
            throw new ServiceException("Maping already exist for "+sb.toString());
        }

    }

    private List<UserMappingDetails> generateNewMappingData(MapUserRequestDto request){

        List<User> userList = getUserList(request);

        Admin currentLoggedInAdmin = getCurrentLoggedInAdmin();

        User user = userList.stream().filter(e -> e.getId() == request.getUserId()).findFirst().get();
        List<User> linekdUserList = userList.stream().filter(e -> e.getId() != request.getUserId()).collect(Collectors.toList());


        List<UserMappingDetails> newMappingList = new ArrayList<>();

        for (User linkeduser : linekdUserList){
            UserMappingDetails userMapping1 = UserMappingDetails.builder().user(user).linkedTo(linkeduser).linkedBy(currentLoggedInAdmin).linkedAt(LocalDateTime.now()).build();
            UserMappingDetails userMapping2 = UserMappingDetails.builder().user(linkeduser).linkedTo(user).linkedBy(currentLoggedInAdmin).linkedAt(LocalDateTime.now()).build();
            newMappingList.add(userMapping1);
            newMappingList.add(userMapping2);
        }
        return newMappingList;
    }
    
    private List<User> getUserList(MapUserRequestDto request){
        List<Long> userIdList = request.getLinkedUsersId();
        userIdList.add(request.getUserId());

        List<User> existedUsersList = userRepository.findByIdInAndStatus(userIdList, Status.ACTIVE.toString());

        List<Long> existedUsersIdList = existedUsersList.stream().map(e -> e.getId()).collect(Collectors.toList());
        
        if (existedUsersIdList.size()==userIdList.size()){
            return existedUsersList;
        }else {
            userIdList.removeAll(existedUsersIdList);
            throw  new ServiceException("Id with "+userIdList+" are inactive users");
        }

    }

    private void saveUserMappingDetails(List<UserMappingDetails> newMappingList){
        userMappingDetailsRepository.saveAll(newMappingList);
    }

    public ResponseDto getSuccessResponseDto(Object data){

        ResponseDto responseDto = new ResponseDto();

        responseDto.setMessage(ResponseStatus.SUCCESS.toString());
        responseDto.setStatus(true);
        responseDto.setData(data);
        return responseDto;
    }

    public ResponseDto getFailureResponseDto(Object data){

        ResponseDto responseDto = new ResponseDto();

        responseDto.setMessage(ResponseStatus.FAILURE.toString());
        responseDto.setStatus(false);
        responseDto.setData(data);
        return responseDto;
    }

    private Admin getCurrentLoggedInAdmin(){
        Admin admin = authenticationUtil.currentLoggedInAdmin();
        return admin;
    }


    private Pageable getPageInfo(SearchUserRequestDto request){

        String sortBy = Objects.isNull(request.getSortBy()) || request.getSortBy().equalsIgnoreCase("")?"id":request.getSortBy();

        String sortType = Objects.isNull(request.getSortType()) || request.getSortType().equalsIgnoreCase("")?"ASC":request.getSortType().toUpperCase();

        Integer offSet = Objects.isNull(request.getOffset()) ?0:request.getOffset();

        Integer pageSize = Objects.isNull(request.getPageSize()) ?0:request.getPageSize();

        Sort sort = Sort.by(sortType.equalsIgnoreCase("DESC")? Sort.Direction.DESC: Sort.Direction.ASC,sortBy);

        return PageRequest.of(offSet,pageSize).withSort(sort);
    }


    private List<SearchCriteria> buildSearchCriteriaList(SearchUserRequestDto request){

        List<String> searchKey = request.getSearchKey();
        List<String> searchValue = request.getSearchValue();

        List<SearchCriteria> criteriaList = new ArrayList<>();

        for (int i=0;i<searchKey.size();i++){
            SearchCriteria searchCriteria = SearchCriteria.builder().key(searchKey.get(i)).value(searchValue.get(i)).build();
            criteriaList.add(searchCriteria);
        }
        return criteriaList;
    }

    private UserSpecification getSearchSpecification(List<SearchCriteria> criteriaList){
        return new UserSpecification(criteriaList);
    }

    private List<User> getAllUsers(SearchUserRequestDto request){

        Pageable pageInfo = getPageInfo(request);
        if(request.isSearchKeyPresent()){


            List<SearchCriteria> criteriaList = buildSearchCriteriaList(request);
            UserSpecification searchSpecification = getSearchSpecification(criteriaList);
            return getAllUsersBySpecification(searchSpecification,pageInfo);


        }else {
            return userRepository.findAll(pageInfo).getContent();
        }




    }

    private List<User> getAllUsersBySpecification(UserSpecification specification,Pageable pageable){

        return userRepository.findAll(specification,pageable);

    }


    private List<User> getAllUsersBySpecification(UserSpecification specification){

        return userRepository.findAll(specification);

    }
    private  List<GetUserResponseDto> getUserResponseDtoList(List<User> allUsers){
       return allUsers.stream().map(e->new GetUserResponseDto(e)).collect(Collectors.toList());
    }


    private Integer getTotalCountBySpecification(SearchUserRequestDto request){

        if(request.isSearchKeyPresent()){
            List<SearchCriteria> criteriaList = buildSearchCriteriaList(request);
            UserSpecification searchSpecification = getSearchSpecification(criteriaList);
            return getAllUsersBySpecification(searchSpecification).size();
        }else {
            return userRepository.findAll().size();
        }
    }
}
