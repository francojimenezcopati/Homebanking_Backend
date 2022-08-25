package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

public final class AccountUtils {

    public AccountUtils(){}
    public static String generateAccountNumber(int min,int max, AccountRepository accountRepository){
        String number;
        int flag=0;
        do {
            int value = (int) ((Math.random() * (max - min)) + min);
            number = "VIN" + value;
            if (accountRepository.findByNumber(number) == null) {
                flag = 1;
            }
        }while (flag != 1) ;
        return number;
    }
}