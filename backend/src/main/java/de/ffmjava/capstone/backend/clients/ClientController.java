package de.ffmjava.capstone.backend.clients;

import de.ffmjava.capstone.backend.clients.model.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clients/")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

    @GetMapping
    List<Client> getAllClients() {
        return service.getAllClients();
    }
}

