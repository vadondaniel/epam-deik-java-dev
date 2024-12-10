package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.account.AccountService;
import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@AllArgsConstructor
public class AccountCommand {

    private final AccountService accountService;
    private final BookingService bookingService;

    @ShellMethod(key = "sign out", value = "Account sign out")
    public void signOut() {
        accountService.signOut();
    }

    @ShellMethod(key = "sign in", value = "Account sign in")
    public String signIn(String username, String password) {
        return accountService.signIn(username, password)
                .map(accountDto -> "Successfully signed in as " + accountDto.username() + "!")
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "sign in privileged", value = "Privileged account sign in")
    public String signInPrivileged(String username, String password) {
        return accountService.signInPrivileged(username, password)
                .map(accountDto -> "Successfully signed in with privileged account '" + accountDto.username() + "'!")
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "describe account", value = "Get account information")
    public String describe() {
        var accountDescription = accountService.describe();
        if (accountDescription.isEmpty()) {
            return "You are not signed in";
        }

        if (accountService.isAdmin()) {
            return "Signed in with privileged account '" + accountDescription.get().username() + "'";
        } else {
            StringBuilder description = new StringBuilder("Signed in with account '"
                    + accountDescription.get().username() + "'");
            List<BookingDto> bookings = bookingService.getBookingsByUsername(accountDescription.get().username());
            if (!bookings.isEmpty()) {
                description.append("\nYour previous bookings are");
                bookings.stream()
                        .collect(Collectors.groupingBy(booking ->
                                booking.movieTitle() + " in room " + booking.roomName() + " starting at "
                                        + booking.startTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
                        .forEach((screening, groupedBookings) -> {
                            String seats = groupedBookings.stream()
                                    .flatMap(booking -> booking.seats().stream())
                                    .map(seat -> "(" + seat.row() + "," + seat.column() + ")")
                                    .collect(Collectors.joining(", "));
                            int totalPrice = groupedBookings.stream()
                                    .mapToInt(BookingDto::price)
                                    .sum();
                            description.append("\nSeats ").append(seats)
                                    .append(" on ").append(groupedBookings.get(0).movieTitle())
                                    .append(" in room ").append(groupedBookings.get(0).roomName())
                                    .append(" starting at ").append(groupedBookings.get(0).startTime()
                                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                                    .append(" for ").append(totalPrice).append(" HUF");
                        });
            } else {
                description.append("\nYou have not booked any tickets yet");
            }
            return description.toString();
        }
    }

    @ShellMethod(key = "sign up", value = "Sign up with a new account")
    public void signUp(String username, String password) {
        accountService.signUp(username, password);
    }

    @ShellMethod(key = "book", value = "Book seats for a movie screening")
    public String book(String movieTitle, String roomName, String startTime, String seats) {
        return accountService.book(movieTitle, roomName, startTime, Arrays.asList(seats.split(" ")));
    }
}
