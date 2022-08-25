package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
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

import java.util.List;
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

    @RequestMapping("/transactions")
    public List<TransactionDTO> getTransactions(){
        return this.transactionRepository.findAll().stream().map(TransactionDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/transactions/{id}")
    public TransactionDTO getTransaction(@PathVariable Long id)
    {
        return this.transactionRepository.findById(id).map(TransactionDTO::new).orElse(null);
    }

    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam String fromAccountNumber
            , @RequestParam String toAccountNumber, @RequestParam double amount , @RequestParam String description){

        Client client = this.clientRepository.findByEmail(authentication.getName());

        if(fromAccountNumber.isEmpty() || toAccountNumber.isEmpty() || amount == 0 || description.isEmpty()){
            return new ResponseEntity<>("Verifique los parametros ingresados.", HttpStatus.FORBIDDEN);
        }

        if(accountRepository.findByNumber(fromAccountNumber) == accountRepository.findByNumber(toAccountNumber)){
            return new ResponseEntity<>("Las cuentas de la transaccion son iguales.", HttpStatus.FORBIDDEN);
        }

        if(accountRepository.findByNumber(fromAccountNumber) == null){
            return new ResponseEntity<>("La cuenta de origen no existe.", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(accountRepository.findByNumber(fromAccountNumber))){
            return new ResponseEntity<>("La cuenta de origen no pertenece al cliente autencicado.", HttpStatus.FORBIDDEN);
        }

        if(accountRepository.findByNumber(toAccountNumber) == null){
            return new ResponseEntity<>("La cuenta de destino no existe.", HttpStatus.FORBIDDEN);
        }

        if(accountRepository.findByNumber(fromAccountNumber).getBalance() < amount){
            return new ResponseEntity<>("No dispone del saldo suficiente para realizar la transaccion.", HttpStatus.FORBIDDEN);
        }

        Transaction fromTransaction = new Transaction(TransactionType.DEBIT, -amount, description, accountRepository.findByNumber(fromAccountNumber));
        Transaction toTransaction = new Transaction(TransactionType.CREDIT, amount, description, accountRepository.findByNumber(toAccountNumber));
        transactionRepository.save(fromTransaction);
        transactionRepository.save(toTransaction);

        Account fromAccount = accountRepository.findByNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByNumber(toAccountNumber);
        fromAccount.setBalance(fromAccount.getBalance()-amount);
        toAccount.setBalance(toAccount.getBalance()+amount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return new ResponseEntity<>("201 Created", HttpStatus.CREATED);
    }
}
