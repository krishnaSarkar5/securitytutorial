package com.secuirityTutorial.common.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HashCardDetailsGeneration {

    @Autowired
    private HashStringGenerator hashStringGenerator;

    @Autowired
    private EncryptorDecryptor encryptorDecryptor;


    public String generateHashEncryptedCardDetails(String cardNumber){
        String hashedCard = hashStringGenerator.generateHashStringforCard(cardNumber);
        String maskedCard = getMaskedCardString(cardNumber);
        String encryptedmaskedCardDetails = getEncryptedCardDetails(maskedCard);
        return hashedCard+"<>"+encryptedmaskedCardDetails;
    }

    private String getMaskedCardString(String cardNumber){
        String last4digits = cardNumber.substring(12);
        return "XXXX-XXXX-XXXX-"+last4digits;
    }

    public String getMaskedCardStringFromHashString(String hashedCard){
        System.out.println(hashedCard);
        String[] splitedStrings = hashedCard.split("<>");
        System.out.println(splitedStrings[1]);
        return getDecryptedCardDetails(splitedStrings[1]);
    }

    private String getEncryptedCardDetails(String cardNumber){
       return encryptorDecryptor.encrypt(cardNumber);
    }

    public String getDecryptedCardDetails(String cardNumber){
        return encryptorDecryptor.decrypt(cardNumber);
    }
}
