package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController // retorna el formato JSON, indica que es un controlador
@RequestMapping("/api")
public class ClientController {
    @Autowired // voy a poder usar el clientrepository para obtener info
    private ClientRepository clientRepository;

    @RequestMapping("/clients") // Cuando se mete esta url, ejecuta el siguiente metodo
    public List<ClientDTO> getClients() {
        return this.clientRepository.findAll().stream() // convierte de variable "lista" a "stream" para poder usar el .map
                .map(ClientDTO::new)// Convierte 1 por 1 a los clientes en objetos de la clase ClientDTO
                .collect(toList());// esto pasa devuelta de variable "stream" a "lista"
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClients(@PathVariable Long id) {
        if(clientRepository.findById(id).isPresent())
            return new ClientDTO(clientRepository.findById(id).get());
        return null;
    }
}
