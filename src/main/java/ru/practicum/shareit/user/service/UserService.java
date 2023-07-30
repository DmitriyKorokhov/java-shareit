package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto getUserById(int id);

    Collection<UserDto> getAllUsers();

    UserDto updateUser(UserDto userDto, int userId);

    void deleteUserById(int id);
}
