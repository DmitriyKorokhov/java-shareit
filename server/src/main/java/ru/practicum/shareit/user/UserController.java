package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        log.info("Добавление нового User");
        return userService.addUser(userDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public UserDto getUserById(@PathVariable int id) {
        log.info("Получение User с id = {}", id);
        return userService.getUserById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<UserDto> getAllUsers() {
        log.info("Вывод всех Users");
        return userService.getAllUsers();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}")
    public UserDto updateUser(@RequestBody UserDto userDto,
                              @PathVariable("id") int id) {
        log.info("Обнавление User с id = {}", id);
        return userService.updateUser(userDto, id);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{id}")
    public void deleteUserById(@PathVariable int id) {
        log.info("Удаление User с id = {}", id);
        userService.deleteUserById(id);
    }
}