package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.repositories.CardRepository;

public final class CardUtils {
    private CardUtils(){}
    public static String generateCardNumber(CardRepository cardRepository){
        String number;
        int min = 1000;
        int max = 9999;
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
    public static int generateCVV() {
        return (int) ((Math.random() * (999 - 100)) + 100);
    }
}
