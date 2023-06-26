package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    User getUserById(int id);

    Collection<User> getAllUsers();

    User addUser(User user);

    User updateUser(User user);

    void deleteUserById(int id);
}
