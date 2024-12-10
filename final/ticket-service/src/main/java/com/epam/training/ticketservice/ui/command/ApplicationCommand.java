package com.epam.training.ticketservice.ui.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ApplicationCommand {

    @ShellMethod(key = "exit", value = "Exit the application")
    public void exit() {
        System.exit(0);
    }
}