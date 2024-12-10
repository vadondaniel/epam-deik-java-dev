package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.booking.model.SeatDto;
import com.epam.training.ticketservice.core.room.model.RoomDto;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    void createRoom(String roomName, int rows, int columns);

    void updateRoom(String roomName, int rows, int columns);

    void deleteRoom(String roomName);

    Optional<List<RoomDto>> listRooms();

    Optional<RoomDto> findByName(String roomName);

    boolean doesSeatExist(String roomName, SeatDto seat);
}
