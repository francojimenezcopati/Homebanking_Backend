package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.AccountUtils.generateAccountNumber;

@RestController
@RequestMapping ("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return this.accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @GetMapping ("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id)
    {
        return this.accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getCurrentClientAccounts(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication, @RequestParam AccountType accountType){
        Client client=this.clientRepository.findByEmail(authentication.getName());
        if(client.getAccounts().size() >= 3){
            return new ResponseEntity<>("403 forbidden", HttpStatus.FORBIDDEN);
        }
        Account account = new Account(generateAccountNumber(accountRepository),0, accountType);
        client.addAccount(account);
        accountRepository.save(account);

        return new ResponseEntity<>("201 created",HttpStatus.CREATED);
    }

    @DeleteMapping("/clients/current/accounts")
    public ResponseEntity<Object> deleteAccount(Authentication authentication, String number){
        Client client=this.clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByNumber(number);
        if(!client.getAccounts().contains(account))
            return new ResponseEntity<>("You don't own this account", HttpStatus.FORBIDDEN);
        if(account.getBalance()>0)
            return new ResponseEntity<>("This account has balance available", HttpStatus.FORBIDDEN);

        transactionRepository.deleteAll(account.getTransactions());
        accountRepository.delete(accountRepository.findByNumber(number));
        return new ResponseEntity<>("Successfully deleted", HttpStatus.ACCEPTED);
    }
}