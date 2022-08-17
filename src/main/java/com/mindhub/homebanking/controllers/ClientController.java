package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController // retorna el formato JSON, indica que es un controlador
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired // voy a poder usar el clientrepository para obtener info
    private ClientRepository clientRepository;

    @RequestMapping("/clients") // Cuando se mete esta url, ejecuta el siguiente metodo
    public List<ClientDTO> getClients() {
        return this.clientRepository.findAll().stream() // convierte de variable "lista" a "stream" para poder usar el .map
                .map(ClientDTO::new)// Convierte 1 por 1 a los clientes en objetos de la clase ClientDTO
                .collect(toList());// esto pasa devuelta de variable "stream" a "lista"
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClients(@PathVariable Long id) {//g
        if(clientRepository.findById(id).isPresent())
            return new ClientDTO(clientRepository.findById(id).get());
        return null;
    }

    @RequestMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        return new ClientDTO(client);
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)

    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {



        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }



        if (clientRepository.findByEmail(email) !=  null) {

            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        }


        clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
