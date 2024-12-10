package com.epam.training.ticketservice.core.booking.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByRoomNameAndSeatRowAndSeatColumnAndStartTime(String roomName, int seatRow, int seatColumn,
                                                                LocalDateTime startTime);

    List<Booking> findByUsername(String username);
}