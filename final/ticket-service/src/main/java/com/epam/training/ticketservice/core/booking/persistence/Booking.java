package com.epam.training.ticketservice.core.booking.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "Bookings")
@Data
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue
    private Integer id;
    String username;
    String movieTitle;
    String roomName;
    int seatRow;
    int seatColumn;
    LocalDateTime startTime;

    public Booking(
            String username,
            String movieTitle,
            String roomName,
            int seatRow,
            int seatColumn,
            LocalDateTime startTime) {
        this.username = username;
        this.movieTitle = movieTitle;
        this.roomName = roomName;
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.startTime = startTime;
    }
}
