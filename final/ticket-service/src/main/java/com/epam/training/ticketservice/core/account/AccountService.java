package com.epam.training.ticketservice.core.account;

import com.epam.training.ticketservice.core.account.model.AccountDto;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    Optional<AccountDto> signIn(String username, String password);

    Optional<AccountDto> signInPrivileged(String username, String password);

    void signOut();

    Optional<AccountDto> describe();

    void signUp(String username, String password);

    void createAdminAccount();

    boolean isAdmin();

    String book(String movieTitle, String roomName, String startTime, List<String> seats);
}