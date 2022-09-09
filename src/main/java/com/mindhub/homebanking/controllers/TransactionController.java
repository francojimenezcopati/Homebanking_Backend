package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions(){
        return this.transactionRepository.findAll().stream().map(TransactionDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/transactions/{id}")
    public TransactionDTO getTransaction(@PathVariable Long id)
    {
        return this.transactionRepository.findById(id).map(TransactionDTO::new).orElse(null);
    }

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createtransaction(Authentication authentication, @RequestParam String fromAccountNumber
            , @RequestParam String toAccountNumber, @RequestParam double amount , @RequestParam String description){

        Client client = this.clientRepository.findByEmail(authentication.getName());

        Account fromAccount = accountRepository.findByNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByNumber(toAccountNumber);
        
        if(fromAccountNumber.isEmpty() || toAccountNumber.isEmpty() || amount == 0 || description.isEmpty()){
            return new ResponseEntity<>("Verifique los parametros ingresados.", HttpStatus.FORBIDDEN);
        }

        if(fromAccount == toAccount){
            return new ResponseEntity<>("Las cuentas de la transaccion son iguales.", HttpStatus.FORBIDDEN);
        }

        if(fromAccount == null){
            return new ResponseEntity<>("La cuenta de origen no existe.", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(fromAccount)){
            return new ResponseEntity<>("La cuenta de origen no pertenece al cliente autencicado.", HttpStatus.FORBIDDEN);
        }

        if(toAccount == null){
            return new ResponseEntity<>("La cuenta de destino no existe.", HttpStatus.FORBIDDEN);
        }

        if(fromAccount.getBalance() < amount){
            return new ResponseEntity<>("No dispone del saldo suficiente para realizar la transaccion.", HttpStatus.FORBIDDEN);
        }

        Transaction fromTransaction = new Transaction(TransactionType.DEBIT, -amount, description, fromAccount);
        Transaction toTransaction = new Transaction(TransactionType.CREDIT, amount, description, toAccount);
        transactionRepository.save(fromTransaction);
        transactionRepository.save(toTransaction);
        
        fromAccount.setBalance(fromAccount.getBalance()-amount);
        toAccount.setBalance(toAccount.getBalance()+amount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return new ResponseEntity<>("201 Created", HttpStatus.CREATED);
    }

    @GetMapping("/transactions/get")
    public Set<TransactionDTO> getTransactionsXToY(Authentication authentication, @RequestParam String number, @RequestParam String fromDate, @RequestParam String thruDate){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByNumber(number);

        LocalDate from = LocalDate.parse ( fromDate ) ;
        LocalDate thru = LocalDate.parse ( thruDate ) ;
        LocalTime localTime = LocalTime.MIDNIGHT ;
        LocalDateTime fromDateTime = LocalDateTime.of ( from , localTime ) ;
        LocalDateTime thruDateTime = LocalDateTime.of ( thru , localTime ) ;

        Set<TransactionDTO> transactionDTOSet = transactionRepository.findByDateBetween(fromDateTime, thruDateTime).stream()
                .filter(transaction -> transaction.getAccount().equals(account)).map(TransactionDTO::new).collect(Collectors.toSet());
        return transactionDTOSet;
    }
}
