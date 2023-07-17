package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Collection<Item> findAllByOwnerOrderById(User user);

    @Query("SELECT i FROM items AS i " +
            "WHERE upper(i.name) LIKE upper(concat('%', :text, '%')) " +
            "OR upper(i.description) LIKE upper(concat('%', :text, '%')) AND i.available = TRUE")
    List<Item> search(@Param("text") String text);
}
