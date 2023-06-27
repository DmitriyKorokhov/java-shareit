package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.validation.exception.UserDataException;
import ru.practicum.shareit.validation.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> mapUsers = new HashMap<>();
    private final List<String> listEmails = new ArrayList<>();
    private int userId = 0;

    private void checkUsersEmail(String email) {
        if (listEmails.contains(email)) {
            throw new UserDataException("Такой Email: " + email + " уже существует");
        }
    }

    @Override
    public void checkUserById(int id) {
        if (!mapUsers.containsKey(id)) {
            throw new ValidationException("User с id = " + id + "не существует");
        }
    }

    @Override
    public User getUserById(int id) {
        checkUserById(id);
        return mapUsers.get(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return mapUsers.values();
    }

    @Override
    public User addUser(User user) {
        checkUsersEmail(user.getEmail());
        user.setId(++userId);
        listEmails.add(user.getEmail());
        mapUsers.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User updatedUser) {
        checkUserById(updatedUser.getId());
        User user = mapUsers.get(updatedUser.getId());
        String newEmail = updatedUser.getEmail();
        String oldEmail = user.getEmail();
        if (newEmail != null && !Objects.equals(newEmail, oldEmail)) {
            checkUsersEmail(newEmail);
            int emailIndex = listEmails.indexOf(oldEmail);
            listEmails.set(emailIndex, newEmail);
            user.setEmail(newEmail);
        }
        String newName = updatedUser.getName();
        if (newName != null) user.setName(newName);
        return user;
    }

    @Override
    public void deleteUserById(int id) {
        checkUserById(id);
        listEmails.remove(mapUsers.get(id).getEmail());
        mapUsers.remove(id);
    }
}
