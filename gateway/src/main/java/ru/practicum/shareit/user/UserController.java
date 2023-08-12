package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.marker.Create;
import ru.practicum.shareit.validation.marker.Update;

@Slf4j
@RequiredArgsConstructor
@Controller
@Validated
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> addUser(@Validated({Create.class}) @RequestBody UserDto userDto) {
        log.info("Добавление нового User");
        return userClient.addUser(userDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public ResponseEntity<Object> getUserById(@PathVariable int id) {
        log.info("Получение User с id = {}", id);
        return userClient.getUserById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Вывод всех Users");
        return userClient.getAllUsers();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}")
    public ResponseEntity<Object> updateUser(@Validated({Update.class}) @RequestBody UserDto userDto,
                                             @PathVariable("id") int id) {
        log.info("Обнавление User с id = {}", id);
        return userClient.updateUser(userDto, id);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable int id) {
        log.info("Удаление User с id = {}", id);
        return userClient.deleteUserById(id);
    }
}