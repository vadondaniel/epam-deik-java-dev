package com.epam.training.ticketservice.core.screening.persistence;

import com.epam.training.ticketservice.core.room.persistence.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Integer> {

    Optional<Screening> findByMovieTitleAndRoomNameAndStartTime(String movieTitle, String roomName, LocalDateTime startTime);

    List<Screening> findAllByRoom(Room room);
}
