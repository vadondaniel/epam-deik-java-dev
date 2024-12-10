package com.epam.training.ticketservice.core.account;

import com.epam.training.ticketservice.core.account.model.AccountDto;
import com.epam.training.ticketservice.core.account.persistence.Account;
import com.epam.training.ticketservice.core.account.persistence.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private AccountDto signedInAccount = null;

    @PostConstruct
    public void init() {
        createAdminAccount();
    }

    @Override
    public Optional<AccountDto> signIn(String username, String password) {
        Optional<Account> user = accountRepository.findByUsernameAndPassword(username, password);
        if (user.isEmpty() || user.get().getRole() == Account.Role.ADMIN) {
            return Optional.empty();
        }
        signedInAccount = new AccountDto(user.get().getUsername(), user.get().getRole());
        return describe();
    }

    @Override
    public Optional<AccountDto> signInPrivileged(String username, String password) {
        Optional<Account> adminAccount = accountRepository.findByUsernameAndPassword(username, password);
        if (adminAccount.isPresent() && adminAccount.get().getRole() == Account.Role.ADMIN) {
            signedInAccount = new AccountDto(adminAccount.get().getUsername(), Account.Role.ADMIN);
            return describe();
        }
        return Optional.empty();
    }

    @Override
    public void signOut() {
        signedInAccount = null;
    }

    @Override
    public Optional<AccountDto> describe() {
        return Optional.ofNullable(signedInAccount);
    }

    @Override
    public void signUp(String username, String password) {
        Account account = new Account(username, password, Account.Role.USER);
        accountRepository.save(account);
    }

    @Override
    public void createAdminAccount() {
        if (accountRepository.findByUsername("admin").isEmpty()) {
            Account adminAccount = new Account("admin", "admin", Account.Role.ADMIN);
            accountRepository.save(adminAccount);
        }
    }
}