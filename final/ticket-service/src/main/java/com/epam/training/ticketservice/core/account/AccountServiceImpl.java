package com.epam.training.ticketservice.core.account;

import com.epam.training.ticketservice.core.account.model.AccountDto;
import com.epam.training.ticketservice.core.account.persistence.Account;
import com.epam.training.ticketservice.core.account.persistence.AccountRepository;
import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.model.SeatDto;
import com.epam.training.ticketservice.core.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final BookingService bookingService;
    private final RoomService roomService;
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

    @Override
    public boolean isAdmin() {
        return describe()
                .map(accountDto -> accountDto.role() == Account.Role.ADMIN)
                .orElse(false);
    }

    @Override
    public String book(String movieTitle, String roomName, String startTime, List<String> seats) {
        if (signedInAccount == null || signedInAccount.role() != Account.Role.USER) {
            return "Booking is only available for signed-in users.";
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime startTimeDate = LocalDateTime.parse(startTime, formatter);

            List<SeatDto> seatDtos = seats.stream()
                    .map(seat -> {
                        String[] parts = seat.split(",");
                        return new SeatDto(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                    })
                    .collect(Collectors.toList());

            for (SeatDto seat : seatDtos) {
                if (!bookingService.isSeatAvailable(roomName, seat, startTimeDate)) {
                    return "Seat (" + seat.row() + "," + seat.column() + ") is already taken";
                }
                if (!roomService.doesSeatExist(roomName, seat)) {
                    return "Seat (" + seat.row() + "," + seat.column() + ") does not exist in this room";
                }
            }

            Optional<BookingDto> booking = bookingService.createBooking(
                    signedInAccount.username(), movieTitle, roomName, startTimeDate, seatDtos);

            if (booking.isPresent()) {
                BookingDto bookingDto = booking.get();
                String seatList = bookingDto.seats().stream()
                        .map(seat -> "(" + seat.row() + "," + seat.column() + ")")
                        .collect(Collectors.joining(", "));
                return "Seats booked: " + seatList + "; the price for this booking is " + bookingDto.price() + " HUF";
            } else {
                return "Booking failed.";
            }
        } catch (DateTimeParseException e) {
            return "Invalid date and time format. Please use YYYY-MM-DD HH:mm";
        }
    }
}