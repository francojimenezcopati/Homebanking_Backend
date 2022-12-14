package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class HomebankingApplication {
    /*@Autowired
    private PasswordEncoder passwordEncoder;*/
    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
        System.out.println("Bienvenido a mi primer servidor");
    }

    /*@Bean // Guarda el metodo como una libreria general que se puede usar en cualquier lado
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository,
                                      LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
        return (args) -> // deavuevle los argumentos que le pase a continuacion
        {
            Client client1 = new Client( "Melba",   "Morel", "melba@mindhub.com", passwordEncoder.encode("123"));
            Client client2 = new Client( "Irene",   "Saenz", "irene@mindhub.com", passwordEncoder.encode("321"));

            Account account1 = new Account(   "VIN00199999", LocalDateTime.now(),5000, AccountType.AHORRO);
            Account account2 = new Account(   "VIN00299999", LocalDateTime.now().plusDays(1), 7500, AccountType.CORRIENTE);
            Account account3 = new Account(   "VIN00399999", LocalDateTime.now(), 9000, AccountType.CORRIENTE);
            Account account4 = new Account(   "VIN00499999", LocalDateTime.now().plusDays(1), 6500, AccountType.AHORRO);

            client1.addAccount(account1);
            client1.addAccount(account2);
            client2.addAccount(account3);
            client2.addAccount(account4);

            clientRepository.save(client1);
            clientRepository.save(client2);
            accountRepository.save(account1);
            accountRepository.save(account2);
            accountRepository.save(account3);
            accountRepository.save(account4);

            Transaction transaction1 = new Transaction(TransactionType.CREDIT, 2000, "transferencia recibida" , account1);
            Transaction transaction2 = new Transaction(TransactionType.DEBIT, -4000, "Compra tienda xx" , account1);
            Transaction transaction3 = new Transaction(TransactionType.CREDIT, 1000, "transferencia recibida" , account2);
            Transaction transaction4 = new Transaction(TransactionType.DEBIT, -200, "Compra tienda xy" , account2);
            Transaction transaction5 = new Transaction(TransactionType.CREDIT, 8000, "transferencia recibida" , account3);
            Transaction transaction6 = new Transaction(TransactionType.DEBIT, -2000, "Compra tienda xz" , account3);
            Transaction transaction7 = new Transaction(TransactionType.CREDIT, 700, "transferencia recibida" , account4);
            Transaction transaction8 = new Transaction(TransactionType.DEBIT, -2000, "Compra tienda xi" , account4);

            transactionRepository.save(transaction1);
            transactionRepository.save(transaction2);
            transactionRepository.save(transaction3);
            transactionRepository.save(transaction4);
            transactionRepository.save(transaction5);
            transactionRepository.save(transaction6);
            transactionRepository.save(transaction7);
            transactionRepository.save(transaction8);

            Loan loan1 = new Loan("Hipotecario",500000, Arrays.asList(12,24,36,48,60), 25);
            Loan loan2 = new Loan("Personal",100000, Arrays.asList(6,12,24),50);
            Loan loan3 = new Loan("Automotriz",300000, Arrays.asList(6,12,24,36),75);

            loanRepository.save(loan1);
            loanRepository.save(loan2);
            loanRepository.save(loan3);

            ClientLoan clientLoan1 = new ClientLoan(400000,60,client1,loan1);
            ClientLoan clientLoan2 = new ClientLoan(50000,12,client1,loan2);
            ClientLoan clientLoan3 = new ClientLoan(100000,24,client2,loan2);
            ClientLoan clientLoan4 = new ClientLoan(200000,36,client2,loan3);

            clientLoanRepository.save(clientLoan1);
            clientLoanRepository.save(clientLoan2);
            clientLoanRepository.save(clientLoan3);
            clientLoanRepository.save(clientLoan4);

            Card card1=new Card(client1,CardType.DEBIT,CardColor.GOLD,"8394-3457-3756-4850",314,LocalDateTime.now(),LocalDateTime.now().plusYears(5));
            Card card2=new Card(client1,CardType.CREDIT,CardColor.TITANIUM,"4326-1894-4618-9574",819,LocalDateTime.now(),LocalDateTime.now().plusYears(5));
            Card card3=new Card(client2,CardType.CREDIT,CardColor.SILVER,"5173-1471-4618-7398",666,LocalDateTime.now(),LocalDateTime.now().plusYears(7));

            cardRepository.save(card1);
            cardRepository.save(card2);
            cardRepository.save(card3);

        };
    }*/
}
