import com.epam.training.ticketservice.core.booking.BookingServiceImpl;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.model.SeatDto;
import com.epam.training.ticketservice.core.booking.persistence.Booking;
import com.epam.training.ticketservice.core.booking.persistence.BookingRepository;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
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

class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ScreeningService screeningService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBookingSuccess() {
        String username = "user";
        String movieTitle = "Movie1";
        String roomName = "Room1";
        LocalDateTime startTime = LocalDateTime.now();
        List<SeatDto> seats = List.of(new SeatDto(1, 1));

        ScreeningDto screeningDto = new ScreeningDto(movieTitle, roomName, startTime);
        when(screeningService.findScreening(movieTitle, roomName, startTime)).thenReturn(Optional.of(screeningDto));
        when(bookingRepository.existsByRoomNameAndSeatRowAndSeatColumnAndStartTime(roomName, 1, 1, startTime)).thenReturn(false);

        Optional<BookingDto> result = bookingService.createBooking(username, movieTitle, roomName, startTime, seats);
        assertTrue(result.isPresent());
        assertEquals(username, result.get().username());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testCreateBookingScreeningNotFound() {
        String username = "user";
        String movieTitle = "Movie1";
        String roomName = "Room1";
        LocalDateTime startTime = LocalDateTime.now();
        List<SeatDto> seats = List.of(new SeatDto(1, 1));

        when(screeningService.findScreening(movieTitle, roomName, startTime)).thenReturn(Optional.empty());

        Optional<BookingDto> result = bookingService.createBooking(username, movieTitle, roomName, startTime, seats);
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateBookingSeatUnavailable() {
        String username = "user";
        String movieTitle = "Movie1";
        String roomName = "Room1";
        LocalDateTime startTime = LocalDateTime.now();
        List<SeatDto> seats = List.of(new SeatDto(1, 1));

        ScreeningDto screeningDto = new ScreeningDto(movieTitle, roomName, startTime);
        when(screeningService.findScreening(movieTitle, roomName, startTime)).thenReturn(Optional.of(screeningDto));
        when(bookingRepository.existsByRoomNameAndSeatRowAndSeatColumnAndStartTime(roomName, 1, 1, startTime)).thenReturn(true);

        Optional<BookingDto> result = bookingService.createBooking(username, movieTitle, roomName, startTime, seats);
        assertFalse(result.isPresent());
    }

    @Test
    void testIsSeatAvailable() {
        String roomName = "Room1";
        SeatDto seat = new SeatDto(1, 1);
        LocalDateTime startTime = LocalDateTime.now();

        when(bookingRepository.existsByRoomNameAndSeatRowAndSeatColumnAndStartTime(roomName, 1, 1, startTime)).thenReturn(false);

        boolean result = bookingService.isSeatAvailable(roomName, seat, startTime);
        assertTrue(result);
    }

    @Test
    void testGetBookingsByUsername() {
        String username = "user";
        Booking booking = new Booking();
        booking.setUsername(username);
        booking.setMovieTitle("Movie1");
        booking.setRoomName("Room1");
        booking.setSeatRow(1);
        booking.setSeatColumn(1);
        booking.setStartTime(LocalDateTime.now());

        when(bookingRepository.findByUsername(username)).thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getBookingsByUsername(username);
        assertEquals(1, result.size());
        assertEquals(username, result.get(0).username());
    }
}