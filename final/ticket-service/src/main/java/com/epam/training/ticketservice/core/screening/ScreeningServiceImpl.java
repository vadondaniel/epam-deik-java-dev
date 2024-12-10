package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
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

    private final ScreeningRepository screeningRepository;

    private final MovieService movieService;
    private final RoomService roomService;

    @Override
    public String createScreening(String movieTitle, String roomName, LocalDateTime startTime) {
        Optional<MovieDto> movie = movieService.findByTitle(movieTitle);
        Optional<RoomDto> room = roomService.findByName(roomName);

        if (movie.isPresent() && room.isPresent()) {
            if (isOverlapping(roomName, startTime, movie.get().length())) {
                return "There is an overlapping screening";
            }
            if (isInBreakPeriod(roomName, startTime, movie.get().length())) {
                return "This would start in the break period after another screening in this room";
            }

            Screening screening = new Screening(movie.get().title(), room.get().name(), startTime);
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
                .map(screening -> new ScreeningDto(screening.getMovieTitle(), screening.getRoomName(), screening.getStartTime()))
                .collect(Collectors.toList());
        return Optional.of(screeningDtos);
    }

    private boolean isOverlapping(String roomName, LocalDateTime startTime, int movieLength) {
        LocalDateTime endTime = startTime.plusMinutes(movieLength);
        List<Screening> screenings = screeningRepository.findAllByRoomName(roomName);

        for (Screening screening : screenings) {
            LocalDateTime existingStartTime = screening.getStartTime();
            LocalDateTime existingEndTime = existingStartTime.plusMinutes(movieService.findByTitle(screening.getMovieTitle()).get().length());

            if (!(startTime.isAfter(existingEndTime) || endTime.isBefore(existingStartTime))) {
                return true;
            }
        }
        return false;
    }

    private boolean isInBreakPeriod(String roomName, LocalDateTime startTime, int movieLength) {
        LocalDateTime endTime = startTime.plusMinutes(movieLength);
        List<Screening> screenings = screeningRepository.findAllByRoomName(roomName);

        for (Screening screening : screenings) {
            LocalDateTime existingStartTime = screening.getStartTime();
            LocalDateTime existingEndTime = existingStartTime.plusMinutes(movieService.findByTitle(screening.getMovieTitle()).get().length());
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
