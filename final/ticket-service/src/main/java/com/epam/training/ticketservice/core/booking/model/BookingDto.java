package com.epam.training.ticketservice.core.booking.model;

import java.time.LocalDateTime;
import java.util.List;

public record BookingDto(String username, String movieTitle, String roomName, LocalDateTime startTime,
                         List<SeatDto> seats, int price) {
}
