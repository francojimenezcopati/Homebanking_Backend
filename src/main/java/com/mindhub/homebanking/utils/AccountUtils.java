package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

public final class AccountUtils {
    @Autowired
    private static AccountRepository accountRepository;
    public AccountUtils(){}
    public static String generateAccountNumber(int min,int max){
        String response;
        int counter=0;
        do {
            int value = (int) ((Math.random() * (max - min)) + min);
            response = "VIN" + value;
            if (accountRepository.findByNumber(response) != null) {
                counter = 1;
            }
        }while (counter != 1) ;
        return response;
    }
}