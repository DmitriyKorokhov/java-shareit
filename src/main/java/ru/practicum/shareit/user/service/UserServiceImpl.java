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
        return UserMapper.INSTANCE.toUserDto(userRepository.save(UserMapper.INSTANCE.toUser(userDto, null)));
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(int id) {
        return UserMapper.INSTANCE.toUserDto(userRepository.findById(id).orElseThrow(() ->
                new ValidationException(HttpStatus.NOT_FOUND, "Объект не найден")));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<UserDto> getAllUsers() {
        return UserMapper.INSTANCE.toListOfUserDto(userRepository.findAll());
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, int userId) {
        User updatedUser = UserMapper.INSTANCE.toUser(userDto, userId);
        User user = userRepository.findById(updatedUser.getId()).orElseThrow();
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }
        return UserMapper.INSTANCE.toUserDto(user);
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }
}