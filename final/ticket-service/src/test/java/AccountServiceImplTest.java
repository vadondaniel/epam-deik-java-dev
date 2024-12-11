import com.epam.training.ticketservice.core.account.AccountServiceImpl;
import com.epam.training.ticketservice.core.account.model.AccountDto;
import com.epam.training.ticketservice.core.account.persistence.Account;
import com.epam.training.ticketservice.core.account.persistence.AccountRepository;
import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.model.SeatDto;
import com.epam.training.ticketservice.core.room.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BookingService bookingService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignInSuccess() {
        Account account = new Account("user", "pass", Account.Role.USER);
        when(accountRepository.findByUsernameAndPassword("user", "pass")).thenReturn(Optional.of(account));

        Optional<AccountDto> result = accountService.signIn("user", "pass");
        assertTrue(result.isPresent());
        assertEquals("user", result.get().username());
    }

    @Test
    void testSignInFailed() {
        when(accountRepository.findByUsernameAndPassword("user", "pass")).thenReturn(Optional.empty());

        Optional<AccountDto> result = accountService.signIn("user", "pass");
        assertFalse(result.isPresent());
    }

    @Test
    void testSignInPrivilegedSuccess() {
        Account account = new Account("admin", "pass", Account.Role.ADMIN);
        when(accountRepository.findByUsernameAndPassword("admin", "pass")).thenReturn(Optional.of(account));

        Optional<AccountDto> result = accountService.signInPrivileged("admin", "pass");
        assertTrue(result.isPresent());
        assertEquals("admin", result.get().username());
    }

    @Test
    void testSignInPrivilegedFailed() {
        when(accountRepository.findByUsernameAndPassword("admin", "pass")).thenReturn(Optional.empty());

        Optional<AccountDto> result = accountService.signInPrivileged("admin", "pass");
        assertFalse(result.isPresent());
    }

    @Test
    void testSignOut() {
        accountService.signOut();
        assertFalse(accountService.describe().isPresent());
    }

    @Test
    void testDescribe() {
        Account account = new Account("user", "pass", Account.Role.USER);
        when(accountRepository.findByUsernameAndPassword("user", "pass")).thenReturn(Optional.of(account));
        accountService.signIn("user", "pass");

        Optional<AccountDto> result = accountService.describe();
        assertTrue(result.isPresent());
        assertEquals("user", result.get().username());
    }

    @Test
    void testSignUp() {
        accountService.signUp("user", "pass");
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testCreateAdminAccount() {
        when(accountRepository.findByUsername("admin")).thenReturn(Optional.empty());

        accountService.createAdminAccount();
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testIsAdmin() {
        Account account = new Account("admin", "pass", Account.Role.ADMIN);
        when(accountRepository.findByUsernameAndPassword("admin", "pass")).thenReturn(Optional.of(account));
        accountService.signInPrivileged("admin", "pass");

        assertTrue(accountService.isAdmin());
    }

    @Test
    void testBookSuccess() {
        Account account = new Account("user", "pass", Account.Role.USER);
        when(accountRepository.findByUsernameAndPassword("user", "pass")).thenReturn(Optional.of(account));
        accountService.signIn("user", "pass");

        when(bookingService.isSeatAvailable(anyString(), any(SeatDto.class), any(LocalDateTime.class))).thenReturn(true);
        when(roomService.doesSeatExist(anyString(), any(SeatDto.class))).thenReturn(true);
        when(bookingService.createBooking(anyString(), anyString(), anyString(), any(LocalDateTime.class), anyList()))
                .thenReturn(Optional.of(new BookingDto("user", "movie", "room", LocalDateTime.now(), List.of(new SeatDto(1, 1)), 1500)));

        String result = accountService.book("movie", "room", "2023-01-01 10:00", List.of("1,1"));
        assertTrue(result.contains("Seats booked"));
    }

    @Test
    void testBookSeatUnavailable() {
        Account account = new Account("user", "pass", Account.Role.USER);
        when(accountRepository.findByUsernameAndPassword("user", "pass")).thenReturn(Optional.of(account));
        accountService.signIn("user", "pass");

        when(bookingService.isSeatAvailable(anyString(), any(SeatDto.class), any(LocalDateTime.class))).thenReturn(false);

        String result = accountService.book("movie", "room", "2023-01-01 10:00", List.of("1,1"));
        assertTrue(result.contains("Seat (1,1) is already taken"));
    }

    @Test
    void testBookSeatDoesNotExist() {
        Account account = new Account("user", "pass", Account.Role.USER);
        when(accountRepository.findByUsernameAndPassword("user", "pass")).thenReturn(Optional.of(account));
        accountService.signIn("user", "pass");

        when(bookingService.isSeatAvailable(anyString(), any(SeatDto.class), any(LocalDateTime.class))).thenReturn(true);
        when(roomService.doesSeatExist(anyString(), any(SeatDto.class))).thenReturn(false);

        String result = accountService.book("movie", "room", "2023-01-01 10:00", List.of("1,1"));
        assertTrue(result.contains("Seat (1,1) does not exist in this room"));
    }

    @Test
    void testBookInvalidDateFormat() {
        Account account = new Account("user", "pass", Account.Role.USER);
        when(accountRepository.findByUsernameAndPassword("user", "pass")).thenReturn(Optional.of(account));
        accountService.signIn("user", "pass");

        String result = accountService.book("movie", "room", "invalid-date", List.of("1,1"));
        assertTrue(result.contains("Invalid date and time format"));
    }

    @Test
    void testBookNotSignedIn() {
        String result = accountService.book("movie", "room", "2023-01-01 10:00", List.of("1,1"));
        assertTrue(result.contains("Booking is only available for signed-in users"));
    }

    @Test
    void testBookAdminSignedIn() {
        accountService.signInPrivileged("admin", "admin");

        String result = accountService.book("movie", "room", "2023-01-01 10:00", List.of("1,1"));
        assertTrue(result.contains("Booking is only available for signed-in users"));
    }
}