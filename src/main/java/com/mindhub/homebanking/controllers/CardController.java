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

import static com.mindhub.homebanking.utils.CardUtils.generateCVV;
import static com.mindhub.homebanking.utils.CardUtils.generateCardNumber;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/cards")
    public List<CardDTO> getCards(){
        return this.cardRepository.findAll().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @GetMapping ("/cards/{id}")
    public CardDTO getCard(@PathVariable Long id)
    {
        return this.cardRepository.findById(id).map(CardDTO::new).orElse(null);
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType cardType, @RequestParam CardColor cardColor){
        Client client=this.clientRepository.findByEmail(authentication.getName());
        if (cardRepository.findByClientAndType(client, cardType).size()>=3) {
            return new ResponseEntity<>("403 forbidden", HttpStatus.FORBIDDEN);
        }
        cardRepository.save(new Card(client, cardType, cardColor, generateCardNumber(cardRepository),generateCVV()));
        return new ResponseEntity<>("201 created",HttpStatus.CREATED);
    }

    @DeleteMapping("/clients/current/cards")
    public ResponseEntity<Object> deleteCard(Authentication authentication, @RequestParam String number){
        Client client=this.clientRepository.findByEmail(authentication.getName());
        //verificar que la tarjeta pertenezca al cliente
        if(!client.getCards().contains(cardRepository.findByNumber(number)))
            return new ResponseEntity<>("You don't own this card", HttpStatus.FORBIDDEN);
        cardRepository.delete(cardRepository.findByNumber(number));
        return new ResponseEntity<>("Successfully deleted", HttpStatus.ACCEPTED);
    }
}
