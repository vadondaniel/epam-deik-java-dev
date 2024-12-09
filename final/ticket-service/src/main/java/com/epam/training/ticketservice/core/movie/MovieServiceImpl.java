package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.movie.persistence.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public void createMovie(String title, String genre, int length) {
        Movie movie = new Movie(title, genre, length);
        movieRepository.save(movie);
    }

    @Override
    public void updateMovie(String title, String genre, int length) {
        Optional<Movie> movieToUpdate = movieRepository.findByTitle(title);
        if (movieToUpdate.isPresent()) {
            Movie movie = movieToUpdate.get();
            movie.setGenre(genre);
            movie.setLength(length);
            movieRepository.save(movie);
        }
    }

    @Override
    public void deleteMovie(String title) {
        Optional<Movie> movieToDelete = movieRepository.findByTitle(title);
        if (movieToDelete.isPresent()) {
            movieRepository.delete(movieToDelete.get());
        }
    }

    @Override
    public Optional<List<MovieDto>> listMovies() {
        List<Movie> movies = movieRepository.findAll();
        if (movies.isEmpty()) {
            return Optional.empty();
        }
        List<MovieDto> movieDtos = movies.stream()
                .map(movie -> new MovieDto(movie.getTitle(), movie.getGenre(), movie.getLength()))
                .collect(Collectors.toList());
        return Optional.of(movieDtos);
    }
}
