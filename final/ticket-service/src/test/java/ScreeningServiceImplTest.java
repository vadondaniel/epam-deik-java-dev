import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.ScreeningServiceImpl;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.Screening;
import com.epam.training.ticketservice.core.screening.persistence.ScreeningRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScreeningServiceImplTest {

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private MovieService movieService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private ScreeningServiceImpl screeningService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateScreeningSuccess() {
        MovieDto movie = new MovieDto("Movie1", "Genre1", 120);
        RoomDto room = new RoomDto("Room1", 10, 10);
        LocalDateTime startTime = LocalDateTime.now();

        when(movieService.findByTitle("Movie1")).thenReturn(Optional.of(movie));
        when(roomService.findByName("Room1")).thenReturn(Optional.of(room));
        when(screeningRepository.findAllByRoomName("Room1")).thenReturn(List.of());

        String result = screeningService.createScreening("Movie1", "Room1", startTime);
        assertEquals("Screening created successfully", result);
        verify(screeningRepository, times(1)).save(any(Screening.class));
    }

    @Test
    void testCreateScreeningMovieOrRoomNotFound() {
        when(movieService.findByTitle("Movie1")).thenReturn(Optional.empty());
        when(roomService.findByName("Room1")).thenReturn(Optional.empty());

        String result = screeningService.createScreening("Movie1", "Room1", LocalDateTime.now());
        assertEquals("Movie or room not found", result);
    }

    @Test
    void testCreateScreeningOverlapping() {
        MovieDto movie = new MovieDto("Movie1", "Genre1", 90);
        RoomDto room = new RoomDto("Room1", 10, 10);
        LocalDateTime startTime = LocalDateTime.now();
        Screening existingScreening = new Screening("Movie2", "Room1", startTime.plusMinutes(60));

        when(movieService.findByTitle("Movie1")).thenReturn(Optional.of(movie));
        when(movieService.findByTitle("Movie2")).thenReturn(Optional.of(new MovieDto("Movie2", "Genre2", 120)));
        when(roomService.findByName("Room1")).thenReturn(Optional.of(room));
        when(screeningRepository.findAllByRoomName("Room1")).thenReturn(List.of(existingScreening));

        String result = screeningService.createScreening("Movie1", "Room1", startTime);
        assertEquals("There is an overlapping screening", result);
    }

    @Test
    void testCreateScreeningInBreakPeriodAfter() {
        MovieDto movie = new MovieDto("Movie1", "Genre1", 90);
        RoomDto room = new RoomDto("Room1", 10, 10);
        LocalDateTime startTime = LocalDateTime.now();
        Screening existingScreening = new Screening("Movie2", "Room1", startTime.minusMinutes(125));

        when(movieService.findByTitle("Movie1")).thenReturn(Optional.of(movie));
        when(movieService.findByTitle("Movie2")).thenReturn(Optional.of(new MovieDto("Movie2", "Genre2", 120)));
        when(roomService.findByName("Room1")).thenReturn(Optional.of(room));
        when(screeningRepository.findAllByRoomName("Room1")).thenReturn(List.of(existingScreening));

        String result = screeningService.createScreening("Movie1", "Room1", startTime);
        assertEquals("This would start in the break period after another screening in this room", result);
    }

    @Test
    void testCreateScreeningInBreakPeriodBefore() {
        MovieDto movie = new MovieDto("Movie1", "Genre1", 90);
        RoomDto room = new RoomDto("Room1", 10, 10);
        LocalDateTime startTime = LocalDateTime.now();
        Screening existingScreening = new Screening("Movie2", "Room1", startTime.plusMinutes(95));

        when(movieService.findByTitle("Movie1")).thenReturn(Optional.of(movie));
        when(movieService.findByTitle("Movie2")).thenReturn(Optional.of(new MovieDto("Movie2", "Genre2", 120)));
        when(roomService.findByName("Room1")).thenReturn(Optional.of(room));
        when(screeningRepository.findAllByRoomName("Room1")).thenReturn(List.of(existingScreening));

        String result = screeningService.createScreening("Movie1", "Room1", startTime);
        assertEquals("This would start in the break period after another screening in this room", result);
    }

    @Test
    void testDeleteScreening() {
        Screening screening = new Screening("Movie1", "Room1", LocalDateTime.now());
        when(screeningRepository.findByMovieTitleAndRoomNameAndStartTime("Movie1", "Room1", screening.getStartTime()))
                .thenReturn(Optional.of(screening));

        screeningService.deleteScreening("Movie1", "Room1", screening.getStartTime());
        verify(screeningRepository, times(1)).delete(screening);
    }

    @Test
    void testListScreenings() {
        assertTrue(screeningService.listScreenings().isEmpty());
        Screening screening = new Screening("Movie1", "Room1", LocalDateTime.now());
        when(screeningRepository.findAll()).thenReturn(List.of(screening));

        Optional<List<ScreeningDto>> screenings = screeningService.listScreenings();
        assertTrue(screenings.isPresent());
        assertEquals(1, screenings.get().size());
        assertEquals("Movie1", screenings.get().get(0).movieTitle());
    }

    @Test
    void testFindScreening() {
        Screening screening = new Screening("Movie1", "Room1", LocalDateTime.now());
        when(screeningRepository.findByMovieTitleAndRoomNameAndStartTime("Movie1", "Room1", screening.getStartTime()))
                .thenReturn(Optional.of(screening));

        Optional<ScreeningDto> screeningDto = screeningService.findScreening("Movie1", "Room1", screening.getStartTime());
        assertTrue(screeningDto.isPresent());
        assertEquals("Movie1", screeningDto.get().movieTitle());
    }
}