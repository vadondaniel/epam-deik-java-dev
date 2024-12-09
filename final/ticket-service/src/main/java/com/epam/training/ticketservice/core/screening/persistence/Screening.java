package com.epam.training.ticketservice.core.screening.persistence;

import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.room.persistence.Room;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Screenings")
@Data
@NoArgsConstructor
public class Screening {

    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    private Movie movie;
    @ManyToOne
    private Room room;
    private LocalDateTime startTime;

    public Screening(Movie movie, Room room, LocalDateTime screeningTime) {
        this.movie = movie;
        this.room = room;
        this.startTime = screeningTime;
    }
}
