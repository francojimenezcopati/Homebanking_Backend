package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
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
public class LoanController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return this.loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @GetMapping(path = "client/current/loans")
    public List<ClientLoanDTO> getCurrentClientLoans(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getClientLoans().stream().map(ClientLoanDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){
        Client client = clientRepository.findByEmail(authentication.getName());
        Loan loan= loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);
        Account account = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
        double amount = loanApplicationDTO.getAmount();
        int payments = loanApplicationDTO.getPayments();
        String toAccountNumber = loanApplicationDTO.getToAccountNumber();
        int interest = loan.getInterest();

        if(amount==0 || payments==0 || loanApplicationDTO == null){
            return new ResponseEntity<>("Verifique los parametros ingresados.", HttpStatus.FORBIDDEN);
        }

        if(loan == null){
            return new ResponseEntity<>("El prestamo no existe.", HttpStatus.FORBIDDEN);
        }

        if(amount > loan.getMaxAmount()){
            return new ResponseEntity<>("El valor ingresado excede el monto maximo permitido.", HttpStatus.FORBIDDEN);
        }

        if(!loan.getPayments().contains(payments)){
            return new ResponseEntity<>("Ingrese un valor de cuotas valido.", HttpStatus.FORBIDDEN);
        }

        if(account == null){
            return new ResponseEntity<>("Verifique los parametros ingresados.", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(account)){
            return new ResponseEntity<>("Verifique los parametros ingresados.", HttpStatus.FORBIDDEN);
        }

        String description = "Loan Approved";
        Transaction transaction = new Transaction(TransactionType.CREDIT,amount,description,account);
        transactionRepository.save(transaction);

        ClientLoan clientLoan = new ClientLoan(amount+(amount*((double)interest/100)),payments,client,loan);
        clientLoanRepository.save(clientLoan);

        account.setBalance(account.getBalance()+amount);
        accountRepository.save(account);

        return new ResponseEntity<>("201 Created", HttpStatus.CREATED);
    }
}
