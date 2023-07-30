package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.exception.ValidationException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IntegrationUserServiceTest {

    private final UserService userService;

    @Test
    public void getUserByIdTest() {
        UserDto userDto = new UserDto(1, "user", "user@mail.com");
        UserDto savedUser = userService.addUser(userDto);
        UserDto gottenUser = userService.getUserById(savedUser.getId());
        assertThat(gottenUser.getId(), notNullValue());
        assertThat(gottenUser.getName(), equalTo(savedUser.getName()));
        assertThat(gottenUser.getEmail(), equalTo(savedUser.getEmail()));
        userService.deleteUserById(gottenUser.getId());
    }

    @Test
    public void getUserByInvalidIdTest() {
        UserDto userDto = new UserDto(1, "user", "user@mail.com");
        userService.addUser(userDto);
        ValidationException exception = assertThrows(ValidationException.class, () -> userService.getUserById(100));
        assertThat(exception.getStatus(), equalTo(HttpStatus.NOT_FOUND));
        assertThat(exception.getMessage(), equalTo("404 NOT_FOUND \"Объект не найден\""));
    }
}