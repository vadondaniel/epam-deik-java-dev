package com.epam.training.ticketservice.core.ui.command;

import com.epam.training.ticketservice.core.account.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@AllArgsConstructor
public class AccountCommand {

    private final AccountService accountService;

    @ShellMethod(key = "sign out", value = "Account sign out")
    public void signOut() {
        accountService.signOut();
    }

    @ShellMethod(key = "sign in", value = "Account sign in")
    public String signIn(String username, String password) {
        return accountService.signIn(username, password)
                .map(accountDto -> "Successfully signed in as " + accountDto.username() + " !")
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "sign in privileged", value = "Privileged account sign in")
    public String signInPrivileged(String username, String password) {
        return accountService.signInPrivileged(username, password)
                .map(accountDto -> "Successfully signed in with privileged account '" + accountDto.username() + "' !")
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "describe account", value = "Get account information")
    public String describe() {
        return accountService.describe()
                .map(accountDto -> "Signed in with privileged account '" + accountDto.username() + "'")
                .orElse("You are not signed in");
    }
}
