package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.PaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PaymentController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/payments")
    public List<PaymentDTO> getPayments(){
        return this.paymentRepository.findAll().stream().map(PaymentDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/payments")
    public ResponseEntity<Object> createPayment(Authentication authentication, @RequestBody PaymentDTO paymentDTO){
        String accountNumber = paymentDTO.getAccountNumber();
        String cardNumber = paymentDTO.getCardNumber();
        String description = paymentDTO.getDescription();
        int CVV = paymentDTO.getCVV();
        double amount = paymentDTO.getAmount();

        Client client = this.clientRepository.findByEmail(authentication.getName());
        Account account = this.accountRepository.findByNumber(accountNumber);
        Card card = this.cardRepository.findByNumber(cardNumber);

        if(accountNumber.isEmpty() || cardNumber.isEmpty() || description.isEmpty() || amount == 0)
            return new ResponseEntity<>("Compruebe los datos ingresados", HttpStatus.FORBIDDEN);
        if(account == null)
            return new ResponseEntity<>("Compruebe los datos de la cuenta", HttpStatus.FORBIDDEN);
        if(card == null)
            return new ResponseEntity<>("Compruebe los datos de la tarjeta", HttpStatus.FORBIDDEN);
        if(!client.getCards().contains(card))
            return new ResponseEntity<>("Compruebe que la tarjeta le corresponda", HttpStatus.FORBIDDEN);
        if(!client.getAccounts().contains(account))
            return new ResponseEntity<>("Compruebe que la cuenta le corresponda", HttpStatus.FORBIDDEN);
        if(amount > account.getBalance())
            return new ResponseEntity<>("Compruebe que su cuenta tenga fondos disponibles", HttpStatus.FORBIDDEN);
        if(card.getThruDate().isBefore(LocalDateTime.now()))
            return new ResponseEntity<>("Su tarjeta esta vencida", HttpStatus.FORBIDDEN);

        Transaction fromTransaction = new Transaction(TransactionType.DEBIT, -amount, description, account);
        transactionRepository.save(fromTransaction);

        account.setBalance(account.getBalance()-amount);
        accountRepository.save(account);



        return new ResponseEntity<>("201 Created", HttpStatus.CREATED);
    }
}
