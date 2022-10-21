package mindhub.homebanking.controllers;


import mindhub.homebanking.Dtos.ClientDto;
import mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
public class ClientController {


    @Autowired
    ClientService clientService;



    @GetMapping("/api/clients")
    public List<ClientDto> getClients(){

        return clientService.getClients().stream().map(client -> new ClientDto(client)).collect(Collectors.toList());
    }



    @GetMapping("/api/clients/{id}")
    public ClientDto getClient(@PathVariable Long id){


            return clientService.getClient(id);



    }



    @PostMapping("/api/clients")

    public ResponseEntity<Object> register(

            @RequestParam String name, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {


        return clientService.register(name, lastName, email, password);


        }


        @GetMapping("/api/clients/current")

        public ClientDto getCurrentClient (Authentication authentication){

            return clientService.getCurrentClient(authentication);


        }



}
