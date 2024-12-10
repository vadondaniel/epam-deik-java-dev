package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.account.AccountService;
import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

@ShellComponent
@AllArgsConstructor
public class MovieCommand {

    private final MovieService movieService;
    private final AccountService accountService;

    @ShellMethod(key = "create movie", value = "Create a new movie")
    public void createMovie(String title, String genre, int length) {
        if (accountService.isAdmin()) {
            movieService.createMovie(title, genre, length);
        } else {
            System.out.println("You need to be an admin to create a movie");
        }
    }

    @ShellMethod(key = "update movie", value = "Update a movie")
    public void updateMovie(String title, String genre, int length) {
        if (accountService.isAdmin()) {
            movieService.updateMovie(title, genre, length);
        } else {
            System.out.println("You need to be an admin to update a movie");
        }
    }

    @ShellMethod(key = "delete movie", value = "Delete a movie")
    public void deleteMovie(String title) {
        if (accountService.isAdmin()) {
            movieService.deleteMovie(title);
        } else {
            System.out.println("You need to be an admin to delete a movie");
        }
    }

    @ShellMethod(key = "list movies", value = "List all movies")
    public String listMovies() {
        List<MovieDto> movies = movieService.listMovies().orElse(List.of());
        if (movies.isEmpty()) {
            return "There are no movies at the moment";
        }
        StringBuilder result = new StringBuilder();
        for (MovieDto movie : movies) {
            result.append(String.format("%s (%s, %d minutes)%n", movie.title(), movie.genre(), movie.length()));
        }
        return result.toString().trim();
    }
}
