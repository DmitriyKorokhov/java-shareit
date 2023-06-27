package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    public UserDto addUser(UserDto userDto) {
        return UserMapper.toUserDto(userStorage.addUser(UserMapper.toUser(userDto, null)));
    }

    public UserDto getUserById(int id) {
        return UserMapper.toUserDto(userStorage.getUserById(id));
    }

    public Collection<UserDto> getAllUsers() {
        return UserMapper.toListOfUserDto(userStorage.getAllUsers());
    }

    public UserDto updateUser(UserDto userDto, int userId) {
        return UserMapper.toUserDto(userStorage.updateUser(UserMapper.toUser(userDto, userId)));
    }

    public void deleteUserById(int id) {
        userStorage.deleteUserById(id);
    }
}
