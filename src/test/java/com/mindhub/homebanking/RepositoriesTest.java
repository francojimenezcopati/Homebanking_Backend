package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TransactionRepository transactionRepository;

    //////Loan tests----------------------------------------------------------------------------------
    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }
    //////------------------------------------------------------------------------------------------------

    //////Account tests----------------------------------------------------------------------------------
    @Test
    public void existAccounts(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts,is(not(empty())));
    }

    @Test
    public void nullBalanceAccount(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts,everyItem(hasProperty("balance", is(greaterThan(0.0)))));
    }
    //////------------------------------------------------------------------------------------------------

    //////Card tests----------------------------------------------------------------------------------
    @Test
    public void existCards(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards,is(not(empty())));
    }

    @Test
    public void existCreditCard(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("type", is(CardType.CREDIT))));
    }
    //////------------------------------------------------------------------------------------------------

    //////Client tests----------------------------------------------------------------------------------
    @Test
    public void existClients(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients,is(not(empty())));
    }

    @Test
    public void existMelbaClient(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("firstName", is("Melba"))));
    }
    //////------------------------------------------------------------------------------------------------

    //////Transaction tests----------------------------------------------------------------------------------
    @Test
    public void existTransactions(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions,is(not(empty())));
    }

    @Test
    public void existDebitTransactions(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("type", is(TransactionType.DEBIT))));
    }
    //////------------------------------------------------------------------------------------------------
}
