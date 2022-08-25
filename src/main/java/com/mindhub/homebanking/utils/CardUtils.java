package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;

public final class CardUtils {
    public CardUtils(){}
    public static String generateCardNumber(int min,int max,CardRepository cardRepository){
        String number;
        int flag=0;
        do {
            int value1 = (int) ((Math.random() * (max - min)) + min);
            int value2 = (int) ((Math.random() * (max - min)) + min);
            int value3 = (int) ((Math.random() * (max - min)) + min);
            int value4 = (int) ((Math.random() * (max - min)) + min);
            number = value1 + "-" + value2 + "-" + value3 + "-" + value4;
            if (cardRepository.findByNumber(number) == null) {
                flag = 1;
            }
        }while (flag != 1) ;
        return number;
    }
}
