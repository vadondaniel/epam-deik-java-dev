package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.account.AccountService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

@ShellComponent
@AllArgsConstructor
public class RoomCommand {

    private final RoomService roomService;
    private final AccountService accountService;

    @ShellMethod(key = "create room", value = "Create a new room")
    public void createRoom(String name, int rows, int columns) {
        if (accountService.isAdmin()) {
            roomService.createRoom(name, rows, columns);
        } else {
            System.out.println("You need to be an admin to create a room");
        }
    }

    @ShellMethod(key = "update room", value = "Update a room")
    public void updateRoom(String name, int rows, int columns) {
        if (accountService.isAdmin()) {
            roomService.updateRoom(name, rows, columns);
        } else {
            System.out.println("You need to be an admin to update a room");
        }
    }

    @ShellMethod(key = "delete room", value = "Delete a room")
    public void deleteRoom(String name) {
        if (accountService.isAdmin()) {
            roomService.deleteRoom(name);
        } else {
            System.out.println("You need to be an admin to delete a room");
        }
    }

    @ShellMethod(key = "list rooms", value = "List all rooms")
    public String listRooms() {
        List<RoomDto> rooms = roomService.listRooms().orElse(List.of());
        if (rooms.isEmpty()) {
            return "There are no rooms at the moment";
        }
        StringBuilder result = new StringBuilder();
        for (RoomDto room : rooms) {
            result.append(String.format("Room %s with %d seats, %d rows and %d columns%n", room.name(), room.columns()*room.rows(), room.rows(), room.columns()));
        }
        return result.toString().trim();
    }
}
