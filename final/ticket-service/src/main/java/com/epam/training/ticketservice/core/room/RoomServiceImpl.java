package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.Room;
import com.epam.training.ticketservice.core.room.persistence.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public void createRoom(String roomName, int rows, int columns) {
        Room room = new Room(roomName, rows, columns);
        roomRepository.save(room);
    }

    @Override
    public void updateRoom(String roomName, int rows, int columns) {
        Optional<Room> roomToUpdate = roomRepository.findByName(roomName);
        if (roomToUpdate.isPresent()) {
            Room room = roomToUpdate.get();
            room.setRows(rows);
            room.setColumns(columns);
            roomRepository.save(room);
        }
    }

    @Override
    public void deleteRoom(String roomName) {
        Optional<Room> roomToDelete = roomRepository.findByName(roomName);
        if (roomToDelete.isPresent()) {
            roomRepository.delete(roomToDelete.get());
        }
    }

    @Override
    public Optional<List<RoomDto>> listRooms() {
        List<Room> rooms = roomRepository.findAll();
        if (rooms.isEmpty()) {
            return Optional.empty();
        }
        List<RoomDto> roomDtos = rooms.stream()
                .map(room -> new RoomDto(room.getName(), room.getRows(), room.getColumns()))
                .collect(Collectors.toList());
        return Optional.of(roomDtos);
    }

    @Override
    public Optional<RoomDto> findByName(String roomName) {
        return roomRepository.findByName(roomName)
                .map(room -> new RoomDto(room.getName(), room.getRows(), room.getColumns()));
    }
}
