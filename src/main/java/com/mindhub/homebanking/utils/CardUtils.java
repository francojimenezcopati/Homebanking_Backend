package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;

public final class CardUtils {
    @Autowired
    private static CardRepository cardRepository;
    public CardUtils(){}
    public static String generateCardNumber(int min,int max){
        String response;
        int counter=0;
        do {
            int value1 = (int) ((Math.random() * (max - min)) + min);
            int value2 = (int) ((Math.random() * (max - min)) + min);
            int value3 = (int) ((Math.random() * (max - min)) + min);
            int value4 = (int) ((Math.random() * (max - min)) + min);
            response = value1 + "-" + value2 + "-" + value3 + "-" + value4;
            if (cardRepository.findByNumber(response) != null) {
                counter = 1;
            }
        }while (counter != 1) ;
        return response;
    }
}
