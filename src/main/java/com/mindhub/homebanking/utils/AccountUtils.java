package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.repositories.AccountRepository;

public final class AccountUtils {

    private AccountUtils(){}
    public static String generateAccountNumber(AccountRepository accountRepository){
        String number;
        int min= 10000000;
        int max= 99999999;
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