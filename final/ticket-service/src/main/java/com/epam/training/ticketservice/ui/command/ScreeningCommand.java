package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class ScreeningCommand {

    private final ScreeningService screeningService;
    private final MovieService movieService;

    @ShellMethod(key = "create screening", value = "Create a new screening")
    public String createScreening(String movieTitle, String roomName, String start) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            LocalDateTime startTime = LocalDateTime.parse(start, formatter);
            return screeningService.createScreening(movieTitle, roomName, startTime);
        } catch (DateTimeParseException e) {
            return "Invalid date and time format. Please use YYYY-MM-DD HH:mm";
        }
    }

    @ShellMethod(key = "delete screening", value = "Delete a screening")
    public void deleteScreening(String movieTitle, String roomName, String start) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            LocalDateTime startTime = LocalDateTime.parse(start, formatter);
            screeningService.deleteScreening(movieTitle, roomName, startTime);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date and time format. Please use YYYY-MM-DD HH:mm");
        }
    }

    @ShellMethod(key = "list screenings", value = "List all screenings")
    public String listScreenings() {
        return screeningService.listScreenings().map(screenings -> {
            StringBuilder result = new StringBuilder();
            for (ScreeningDto screening : screenings) {
                Optional<MovieDto> movie = movieService.findByTitle(screening.movieTitle());
                if (movie.isPresent()) {
                    result.append(String.format("%s (%s, %d minutes), screened in room %s, at %s%n",
                            screening.movieTitle(), movie.get().genre(), movie.get().length(), screening.roomName(),
                            screening.startDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
                }
            }
            return result.toString().trim();
        }).orElse("There are no screenings");
    }
}
