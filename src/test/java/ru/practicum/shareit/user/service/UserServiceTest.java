package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUserTest() {
        User user = new User(1, "user1", "user1@mail.com");
        UserDto userDto = UserMapper.toUserDto(user);
        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(user));
        UserDto gottenUser = userService.getUserById(1);
        assertNotNull(gottenUser);
        assertEquals(gottenUser, userDto);
        verify(userRepository, times(1)).findById(any(Integer.class));
    }

    @Test
    void getAllUsersTest() {
        User user1 = new User(1, "user1", "user1@mail.com");
        User user2 = new User(2, "user2", "user2@mail.com");
        UserDto userDto1 = UserMapper.toUserDto(user1);
        UserDto userDto2 = UserMapper.toUserDto(user2);
        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));
        Collection<UserDto> users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(2, users.size());
        assertTrue(users.contains(userDto1));
        assertTrue(users.contains(userDto2));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void createUserTest() {
        User user = new User(1, "user1", "user1@mail.com");
        UserDto userDto = UserMapper.toUserDto(user);
        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        UserDto createdUser = userService.addUser(UserMapper.toUserDto(user));
        assertNotNull(createdUser);
        assertEquals(userDto, createdUser);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUserTest() {
        User user = new User(1, "user1", null);
        UserDto userDto = UserMapper.toUserDto(user);
        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(user));
        UserDto updatedUser = userService.updateUser(UserMapper.toUserDto(user), 1);
        assertNotNull(updatedUser);
        assertEquals(updatedUser, userDto);
        verify(userRepository, times(1)).findById(any(Integer.class));
    }

    @Test
    void deleteUserByIdTest() {
        userService.deleteUserById(1);
        verify(userRepository, times(1)).deleteById(1);
    }
}