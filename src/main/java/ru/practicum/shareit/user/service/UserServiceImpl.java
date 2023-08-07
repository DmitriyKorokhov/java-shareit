package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.exception.ValidationException;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto, null)));
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(int id) {
        return UserMapper.toUserDto(userRepository.findById(id).orElseThrow(() ->
                new ValidationException(HttpStatus.NOT_FOUND, "Объект не найден")));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<UserDto> getAllUsers() {
        return UserMapper.toListOfUserDto(userRepository.findAll());
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, int userId) {
        User updatedUser = UserMapper.toUser(userDto, userId);
        User user = userRepository.findById(updatedUser.getId()).orElseThrow();
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }
}