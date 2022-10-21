package mindhub.homebanking.services;

import mindhub.homebanking.Dtos.ClientDto;
import mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {

    public List<Client> getClients();

    public ClientDto getClient(Long id);

    ResponseEntity<Object> register(String name, String lastName, String email, String password);


    ClientDto getCurrentClient(Authentication authentication);
}
