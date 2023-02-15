package com.secuirityTutorial.user.serviceImpl;

import com.secuirityTutorial.authentication.dto.ResponseDto;
import com.secuirityTutorial.common.dto.Response;
import com.secuirityTutorial.common.dto.SearchCriteria;
import com.secuirityTutorial.common.enums.Status;
import com.secuirityTutorial.common.exception.ServiceException;
import com.secuirityTutorial.common.utility.AuthenticationUtil;
import com.secuirityTutorial.user.dto.request.ConnectUserRequestDto;
import com.secuirityTutorial.user.dto.request.IdDto;
import com.secuirityTutorial.user.dto.reposne.GetUserResponseDto;
import com.secuirityTutorial.user.dto.request.SearchUserRequestDto;
import com.secuirityTutorial.user.entity.MeetingConnectInformation;
import com.secuirityTutorial.payment.entity.PaymentInformation;
import com.secuirityTutorial.user.entity.User;
import com.secuirityTutorial.user.entity.UserMappingDetails;
import com.secuirityTutorial.user.repository.MeetingConnectionInformationRepository;
import com.secuirityTutorial.payment.repository.PaymentInformationRepository;
import com.secuirityTutorial.user.repository.UserMappingDetailsRepository;
import com.secuirityTutorial.user.service.UserService;
import com.secuirityTutorial.user.specifications.UserMappingDetailsSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthenticationUtil authenticationUtil;


    @Autowired
    private UserMappingDetailsRepository userMappingDetailsRepository;


    @Autowired
    private MeetingConnectionInformationRepository meetingConnectionInformationRepository;

    @Autowired
    private PaymentInformationRepository paymentInformationRepository;


    @Override
    public ResponseDto getAContactDetails(IdDto request) {

        try {
            validateIncomingRequest(request);
            GetUserResponseDto contactUser = getContactUser(request.getId());
            return new Response().getSuccessResponseDto(contactUser);
        }catch (Exception e){
            e.printStackTrace();
            return new Response().getFailureResponseDto("Something went wrong");
        }

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

    @Override
    public ResponseDto connectToUser(ConnectUserRequestDto request) {

        validateIncomingRequest(request);

        checkMappingExist(request.getUserId());

        PaymentInformation paymentInformation = checkPaymentIsDoneOrNot(request.getTransactionId(), request.getUserId());

        establishedConnect(paymentInformation);

        return new Response().getSuccessResponseDto("Connecting to "+paymentInformation.getPaymentFor().getFirstName()+" "+paymentInformation.getPaymentFor().getLastName()+" ...");
    }


    private void validateIncomingRequest(Object object){

        if(object instanceof IdDto){
            IdDto request = (IdDto) object;
            request.validate();
        } else if(object instanceof SearchUserRequestDto){
            SearchUserRequestDto request = (SearchUserRequestDto) object;
            request.validate();
        }else if(object instanceof ConnectUserRequestDto){
            ConnectUserRequestDto request = (ConnectUserRequestDto) object;
            request.validate();
        }

        else {
            throw new ServiceException("Something went wrong");
        }
    }




    private GetUserResponseDto getContactUser(Long contactUserId){
        UserMappingDetails userMappingDetails = checkMappingExist(contactUserId);
        return new GetUserResponseDto(userMappingDetails.getLinkedTo());
    }

    private User getCurrentLoggedInUser(){
        return authenticationUtil.currentLoggedInUser();
    }

    private UserMappingDetails checkMappingExist(Long contactUserId){

        User currentLoggedInUser = getCurrentLoggedInUser();
        return userMappingDetailsRepository.findByUserIdAndLinkedToIdAndLinkedToStatus(currentLoggedInUser.getId(), contactUserId, Status.ACTIVE.toString()).orElseThrow(() -> new ServiceException("Invalid user id or This user is not present your contact list"));
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

    private UserMappingDetailsSpecification getSearchSpecification(List<SearchCriteria> criteriaList){
        return new UserMappingDetailsSpecification(criteriaList,getCurrentLoggedInUser());
    }

    private List<User> getAllUsers(SearchUserRequestDto request){

        Pageable pageInfo = getPageInfo(request);
        if(request.isSearchKeyPresent()){


            List<SearchCriteria> criteriaList = buildSearchCriteriaList(request);
            UserMappingDetailsSpecification searchSpecification = getSearchSpecification(criteriaList);
            return getAllUsersBySpecification(searchSpecification,pageInfo).stream().map(e->e.getLinkedTo()).collect(Collectors.toList());


        }else {
            return userMappingDetailsRepository.findAllByUserId(getCurrentLoggedInUser().getId(),pageInfo).stream().map(e->e.getLinkedTo()).collect(Collectors.toList());
        }




    }

    private List<UserMappingDetails> getAllUsersBySpecification(UserMappingDetailsSpecification specification,Pageable pageable){

        return userMappingDetailsRepository.findAll(specification,pageable);

    }


    private List<UserMappingDetails> getAllUsersBySpecification(UserMappingDetailsSpecification specification){

        return userMappingDetailsRepository.findAll(specification);

    }
    private  List<GetUserResponseDto> getUserResponseDtoList(List<User> allUsers){
        return allUsers.stream().map(e->new GetUserResponseDto(e)).collect(Collectors.toList());
    }


    private Integer getTotalCountBySpecification(SearchUserRequestDto request){

        if(request.isSearchKeyPresent()){
            List<SearchCriteria> criteriaList = buildSearchCriteriaList(request);
            UserMappingDetailsSpecification searchSpecification = getSearchSpecification(criteriaList);
            return getAllUsersBySpecification(searchSpecification).size();
        }else {
            return userMappingDetailsRepository.findAllByUserId(getCurrentLoggedInUser().getId()).size();
        }
    }



    private PaymentInformation checkPaymentIsDoneOrNot(String transactionId,Long userId){
        Optional<PaymentInformation> existedPaymentInfoOptional = paymentInformationRepository.findByTransactionIdAndPaymentByIdAndPaymentForId(transactionId,getCurrentLoggedInUser().getId() ,userId);

        if(existedPaymentInfoOptional.isEmpty()){
            throw new ServiceException("Invalid Transaction id. No payment record exist");
        }

        if(existedPaymentInfoOptional.get().isMeetingConnect()){
            throw new ServiceException("Invalid Transaction id. Meeting already completed");
        }

        return existedPaymentInfoOptional.get();

    }


    private void establishedConnect(PaymentInformation paymentInformation){

        MeetingConnectInformation meetingConnectInformation = new MeetingConnectInformation(paymentInformation);

        meetingConnectionInformationRepository.save(meetingConnectInformation);

        paymentInformation.setMeetingConnect(true);

        paymentInformationRepository.save(paymentInformation);
    }

}
