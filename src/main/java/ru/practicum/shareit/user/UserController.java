package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.validation.marker.Create;
import ru.practicum.shareit.validation.marker.Update;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto add(@Validated({Create.class}) @RequestBody UserDto userDto) {
        log.info("Новый User добавлен");
        User user = userService.addUser(UserMapper.toUser(userDto, null));
        return UserMapper.toUserDto(user);
    }

    @GetMapping
    public Collection<UserDto> getAll() {
        log.info("Вывод всех Users");
        Collection<User> users = userService.getAllUsers();
        return UserMapper.toUserDto(users);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable int id) {
        log.info("User с id = " + id + " получен");
        User user = userService.getUserById(id);
        return UserMapper.toUserDto(user);
    }

    @PatchMapping("/{id}")
    public UserDto update(@Validated({Update.class}) @RequestBody UserDto userDto,
                          @PathVariable("id") int id) {
        log.info("User с id = " + id + " обновлен");
        User user = userService.updateUser(UserMapper.toUser(userDto, id));
        return UserMapper.toUserDto(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        log.info("User с id = " + id + " удален");
        userService.deleteUserById(id);
    }
}
