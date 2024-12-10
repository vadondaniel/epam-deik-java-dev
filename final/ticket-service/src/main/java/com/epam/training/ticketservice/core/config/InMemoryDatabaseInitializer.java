package com.epam.training.ticketservice.core.config;

import com.epam.training.ticketservice.core.account.persistence.Account;
import com.epam.training.ticketservice.core.account.persistence.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InMemoryDatabaseInitializer {

    private final AccountRepository userRepository;

    public void init() {
        Account admin = new Account("admin", "admin", Account.Role.ADMIN);
        userRepository.save(admin);
    }
}
