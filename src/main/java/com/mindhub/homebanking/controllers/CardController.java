package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.CardUtils.generateCardNumber;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/cards")
    public List<CardDTO> getCards(){
        return this.cardRepository.findAll().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @RequestMapping ("/cards/{id}")
    public CardDTO getCard(@PathVariable Long id)
    {
        return this.cardRepository.findById(id).map(CardDTO::new).orElse(null);
    }

    @RequestMapping(path= "/clients/current/cards", method = RequestMethod.POST) /////////////////////////////////////////////////////////////////////////// problema
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType cardType, @RequestParam CardColor cardColor){
        Client client=this.clientRepository.findByEmail(authentication.getName());
        if (cardRepository.findByClientAndType(client, cardType).size()>=3) {
            return new ResponseEntity<>("403 forbidden", HttpStatus.FORBIDDEN);
        }
        cardRepository.save(new Card(client, cardType, cardColor, generateCardNumber(1000,9999, cardRepository)));
        return new ResponseEntity<>("201 created",HttpStatus.CREATED);
    }
}
