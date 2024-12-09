package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.movie.persistence.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.Room;
import com.epam.training.ticketservice.core.room.persistence.RoomRepository;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.Screening;
import com.epam.training.ticketservice.core.screening.persistence.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {

    private ScreeningRepository screeningRepository;
    private MovieRepository movieRepository;
    private RoomRepository roomRepository;

    @Override
    public String createScreening(String movieTitle, String roomName, LocalDateTime startTime) {
        Optional<Movie> movie = movieRepository.findByTitle(movieTitle);
        Optional<Room> room = roomRepository.findByName(roomName);

        if (movie.isPresent() && room.isPresent()) {
            if (isOverlapping(room.get(), startTime, movie.get().getLength())) {
                return "There is an overlapping screening";
            }
            if (isInBreakPeriod(room.get(), startTime, movie.get().getLength())) {
                return "This would start in the break period after another screening in this room";
            }

            Screening screening = new Screening(movie.get(), room.get(), startTime);
            screeningRepository.save(screening);
            return "Screening created successfully";
        }
        return "Movie or room not found";
    }

    @Override
    public void deleteScreening(String movieTitle, String roomName, LocalDateTime screeningTime) {
        Optional<Screening> screening = screeningRepository.findByMovieTitleAndRoomNameAndStartTime(movieTitle, roomName, screeningTime);
        if (screening.isPresent()) {
            screeningRepository.delete(screening.get());
        }
    }

    @Override
    public Optional<List<ScreeningDto>> listScreenings() {
        List<Screening> screenings = screeningRepository.findAll();
        if (screenings.isEmpty()) {
            return Optional.empty();
        }
        List<ScreeningDto> screeningDtos = screenings.stream()
                .map(screening -> new ScreeningDto(screening.getMovie().getTitle(), screening.getRoom().getName(), screening.getStartTime()))
                .collect(Collectors.toList());
        return Optional.of(screeningDtos);
    }

    private boolean isOverlapping(Room room, LocalDateTime startTime, int movieLength) {
        LocalDateTime endTime = startTime.plusMinutes(movieLength);
        List<Screening> screenings = screeningRepository.findAllByRoom(room);

        for (Screening screening : screenings) {
            LocalDateTime existingStartTime = screening.getStartTime();
            LocalDateTime existingEndTime = existingStartTime.plusMinutes(screening.getMovie().getLength());

            if (!(startTime.isAfter(existingEndTime) || endTime.isBefore(existingStartTime))) {
                return true;
            }
        }
        return false;
    }

    private boolean isInBreakPeriod(Room room, LocalDateTime startTime, int movieLength) {
        LocalDateTime endTime = startTime.plusMinutes(movieLength);
        List<Screening> screenings = screeningRepository.findAllByRoom(room);

        for (Screening screening : screenings) {
            LocalDateTime existingStartTime = screening.getStartTime();
            LocalDateTime existingEndTime = existingStartTime.plusMinutes(screening.getMovie().getLength());
            LocalDateTime breakEndTime = existingEndTime.plusMinutes(10);
            LocalDateTime breakStartTime = existingStartTime.minusMinutes(10);

            // Check if the new screening starts in the break period after an existing screening
            if (startTime.isAfter(existingEndTime) && startTime.isBefore(breakEndTime)) {
                return true;
            }

            // Check if the new screening ends in the break period before an existing screening
            if (endTime.isAfter(breakStartTime) && endTime.isBefore(existingStartTime)) {
                return true;
            }
        }
        return false;
    }
}
