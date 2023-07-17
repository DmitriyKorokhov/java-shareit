package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {
    void checkUserById(int id);

    User addUser(User user);

    User getUserById(int id);

    Collection<User> getAllUsers();

    User updateUser(User user);

    void deleteUserById(int id);
}
