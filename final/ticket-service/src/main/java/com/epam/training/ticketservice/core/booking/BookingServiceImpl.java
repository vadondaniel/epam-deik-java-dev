package com.epam.training.ticketservice.core.booking;

import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.model.SeatDto;
import com.epam.training.ticketservice.core.booking.persistence.Booking;
import com.epam.training.ticketservice.core.booking.persistence.BookingRepository;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private static final int PRICE_PER_SEAT = 1500;
    private final BookingRepository bookingRepository;
    private final ScreeningService screeningService;

    @Override
    public Optional<BookingDto> createBooking(
            String username,
            String movieTitle,
            String roomName,
            LocalDateTime startTime,
            List<SeatDto> seats) {
        Optional<ScreeningDto> screening = screeningService.findScreening(movieTitle, roomName, startTime);
        if (screening.isEmpty()) {
            return Optional.empty();
        }

        List<SeatDto> unavailableSeats = seats.stream()
                .filter(seat -> !isSeatAvailable(roomName, seat, startTime))
                .collect(Collectors.toList());

        if (!unavailableSeats.isEmpty()) {
            return Optional.empty();
        }

        int totalPrice = seats.size() * PRICE_PER_SEAT;

        BookingDto booking = new BookingDto(username, movieTitle, roomName, startTime, seats, totalPrice);

        for (SeatDto seat : seats) {
            Booking bookingSave = new Booking();
            bookingSave.setUsername(username);
            bookingSave.setMovieTitle(movieTitle);
            bookingSave.setRoomName(roomName);
            bookingSave.setSeatRow(seat.row());
            bookingSave.setSeatColumn(seat.column());
            bookingSave.setStartTime(startTime);
            bookingRepository.save(bookingSave);
        }

        return Optional.of(booking);
    }

    public boolean isSeatAvailable(String roomName, SeatDto seat, LocalDateTime startTime) {
        return !bookingRepository
                .existsByRoomNameAndSeatRowAndSeatColumnAndStartTime(
                        roomName, seat.row(), seat.column(), startTime);
    }

    public List<BookingDto> getBookingsByUsername(String username) {
        List<Booking> bookings = bookingRepository.findByUsername(username);
        return bookings.stream()
                .map(booking -> new BookingDto(
                        booking.getUsername(),
                        booking.getMovieTitle(),
                        booking.getRoomName(),
                        booking.getStartTime(),
                        List.of(new SeatDto(booking.getSeatRow(), booking.getSeatColumn())),
                        PRICE_PER_SEAT
                ))
                .collect(Collectors.toList());
    }
}
