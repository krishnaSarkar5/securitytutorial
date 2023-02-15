package com.secuirityTutorial.payment.serviceImpl;

import com.secuirityTutorial.authentication.dto.ResponseDto;
import com.secuirityTutorial.common.dto.Response;
import com.secuirityTutorial.common.enums.PaymentMode;
import com.secuirityTutorial.common.enums.Status;
import com.secuirityTutorial.common.exception.ServiceException;
import com.secuirityTutorial.common.utility.AuthenticationUtil;
import com.secuirityTutorial.common.utility.HashCardDetailsGeneration;
import com.secuirityTutorial.payment.entity.CardDetails;
import com.secuirityTutorial.payment.entity.PaymentInformation;
import com.secuirityTutorial.user.dto.request.DoPaymentRequestDto;
import com.secuirityTutorial.user.entity.User;
import com.secuirityTutorial.payment.repository.CardDetailsRepository;
import com.secuirityTutorial.payment.repository.PaymentInformationRepository;
import com.secuirityTutorial.user.entity.UserMappingDetails;
import com.secuirityTutorial.user.repository.UserMappingDetailsRepository;
import com.secuirityTutorial.user.repository.UserRepository;
import com.secuirityTutorial.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private AuthenticationUtil authenticationUtil;


    @Autowired
    private CardDetailsRepository cardDetailsRepository;

    @Autowired
    private PaymentInformationRepository paymentInformationRepository;

    @Autowired
    private HashCardDetailsGeneration hashCardDetailsGeneration;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMappingDetailsRepository userMappingDetailsRepository;

    @Override
    @Transactional
    public ResponseDto doPayment(DoPaymentRequestDto request) {
        validateIncomingRequest(request);

        Optional<CardDetails> cardDetails = checkCardExistance(request.getNumber());

        User userToBeConnect = checkMappingExist(request.getUserId()).getLinkedTo();

        PaymentInformation paymentInformation = processForPayment(request, cardDetails, userToBeConnect);

        Map<String,String> responseMap = new HashMap<>();

        String maskedCard = hashCardDetailsGeneration.getMaskedCardStringFromHashString(paymentInformation.getCardDetails().getCardNumber());

        responseMap.put("TransactionId",paymentInformation.getTransactionId());
        responseMap.put("message","Rs "+paymentInformation.getAmount()+" successfully deducted from "+paymentInformation.getPaymentMode()+" no "+maskedCard);


        return new Response().getSuccessResponseDto(responseMap);
    }

    private PaymentInformation processForPayment(DoPaymentRequestDto request, Optional<CardDetails> cardDetails, User userToBeConnect) {
        PaymentInformation paymentInformation;
        if (cardDetails.isEmpty()){
            // create new card
            paymentInformation = generateNewCardAndMakePayment(request, userToBeConnect);
        }else {
            boolean isBalanceAvailable = balanceCheck(cardDetails.get(), userToBeConnect);

            if (isBalanceAvailable){
                paymentInformation =   makePayment(request, cardDetails.get(), userToBeConnect);
            }else {
                // create new card
                throw new ServiceException("This card has no efficient balance to process");
            }
        }
        return paymentInformation;
    }

    private PaymentInformation generateNewCardAndMakePayment(DoPaymentRequestDto request, User userToBeConnect) {
        PaymentInformation paymentInformation;
        CardDetails newCard = getNewCard(request);
        paymentInformation = makePayment(request, newCard, userToBeConnect);
        return paymentInformation;
    }

    private CardDetails getNewCard(DoPaymentRequestDto request){
        CardDetails newCard = CardDetails.builder().cardNumber(hashCardDetailsGeneration.generateHashEncryptedCardDetails(request.getNumber()))
                .type(PaymentMode.valueOf(request.getPaymentMode().toUpperCase()).toString())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(getCurrentLoggedInUser())
                .balance(100)
                .build();

        CardDetails savedCard = cardDetailsRepository.save(newCard);

        return savedCard;
    }

    private PaymentInformation makePayment(DoPaymentRequestDto request, CardDetails cardDetails, User userToBeConnect) {

        PaymentInformation paymentInformation =   initiatePayment(request, userToBeConnect,cardDetails);
        updateCardBalance(cardDetails, userToBeConnect.getCallCharge());
        return paymentInformation;
    }


    private void validateIncomingRequest(Object object){

        if(object instanceof DoPaymentRequestDto){
            DoPaymentRequestDto request = (DoPaymentRequestDto) object;
            request.validate();
        }else {
            throw new ServiceException("Something went wrong");
        }
    }


    private Optional<CardDetails> checkCardExistance(String cardNumber){
        String hashedCard = hashCardDetailsGeneration.generateHashEncryptedCardDetails(cardNumber);
        return  cardDetailsRepository.findByCardNumberAndUserId(hashedCard, getCurrentLoggedInUser().getId());

    }

    private boolean balanceCheck(CardDetails cardDetails,User userToBeConnect){

        if(cardDetails.getBalance()>= userToBeConnect.getCallCharge())
            return true;
        return false;
    }

    private void updateCardBalance(CardDetails cardDetails,Integer amount){
        cardDetails.setBalance(cardDetails.getBalance()-amount);
        cardDetails.setUpdatedAt(LocalDateTime.now());
        cardDetailsRepository.save(cardDetails);
    }



    private User getCurrentLoggedInUser(){
        return authenticationUtil.currentLoggedInUser();
    }

    private User getUserToBeConnect(Long userId){
        return userRepository.findById(userId).orElseThrow(()-> new ServiceException("User not found with id "+ userId));
    }

    private PaymentInformation initiatePayment(DoPaymentRequestDto request, User userToBeConnect,CardDetails cardDetails){
        PaymentInformation paymentInformation = new PaymentInformation(request, userToBeConnect, getCurrentLoggedInUser(),cardDetails);
        return paymentInformationRepository.save(paymentInformation);
    }

    private UserMappingDetails checkMappingExist(Long contactUserId){

        User currentLoggedInUser = getCurrentLoggedInUser();
        return userMappingDetailsRepository.findByUserIdAndLinkedToIdAndLinkedToStatus(currentLoggedInUser.getId(), contactUserId, Status.ACTIVE.toString()).orElseThrow(() -> new ServiceException("Invalid user id or This user is not present your contact list"));
    }
}
