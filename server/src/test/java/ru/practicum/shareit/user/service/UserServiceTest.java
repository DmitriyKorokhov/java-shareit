package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validation.exception.ValidationException;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private User user1;
    private User user2;

    @BeforeEach
    void beforeEach() {
        user1 = new User(1, "user1", "user1@mail.com");
        user2 = new User(2, "user2", "user2@mail.com");
    }

    @Test
    void getUserTest() {
        UserDto userDto = UserMapper.toUserDto(user1);
        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(user1));
        UserDto gottenUser = userService.getUserById(1);
        assertNotNull(gottenUser);
        assertEquals(gottenUser, userDto);
        verify(userRepository, times(1)).findById(any(Integer.class));
    }

    @Test
    void getAllUsersTest() {
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
        UserDto userDto = UserMapper.toUserDto(user1);
        when(userRepository.save(any(User.class)))
                .thenReturn(user1);
        UserDto createdUser = userService.addUser(UserMapper.toUserDto(user1));
        assertNotNull(createdUser);
        assertEquals(userDto, createdUser);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUserWithInvalidEmailValidationTest() {
        UserDto userDto = UserMapper.toUserDto(user1);
        userDto.setEmail("invalidEmail");
        when(userRepository.save(any())).thenThrow(ValidationException.class);
        assertThatThrownBy(() -> userService.addUser(userDto)).isInstanceOf(ValidationException.class);
    }

    @Test
    void updateUserTest() {
        UserDto userDto = UserMapper.toUserDto(user1);
        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(user1));
        UserDto updatedUser = userService.updateUser(UserMapper.toUserDto(user1), 1);
        assertNotNull(updatedUser);
        assertEquals(updatedUser, userDto);
        verify(userRepository, times(1)).findById(any(Integer.class));
    }

    @Test
    void updateUserByIdWithExceptionTest() {
        UserDto userDtoThree = new UserDto(3, "user3", "user3@mail.com");
        UserDto userDto = UserMapper.toUserDto(user1);
        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.updateUser(userDto, userDtoThree.getId())).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void updateUserByIdWithAlreadyExistsExceptionValidationTest() {
        UserDto userDtoThree = new UserDto(3, "user3", "user3@mail.com");
        when(userRepository.findById(userDtoThree.getId())).thenThrow(NoSuchElementException.class);
        assertThatThrownBy(() -> userService.updateUser(userDtoThree, userDtoThree.getId())).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void deleteUserByIdTest() {
        userService.deleteUserById(1);
        verify(userRepository, times(1)).deleteById(1);
    }
}