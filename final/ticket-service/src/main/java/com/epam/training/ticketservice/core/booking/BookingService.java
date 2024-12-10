package com.epam.training.ticketservice.core.booking;

import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.model.SeatDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    Optional<BookingDto> createBooking(
            String username,
            String movieTitle,
            String roomName,
            LocalDateTime startTime,
            List<SeatDto> seats);

    boolean isSeatAvailable(String roomName, SeatDto seat, LocalDateTime startTimeDate);

    List<BookingDto> getBookingsByUsername(String username);
}
