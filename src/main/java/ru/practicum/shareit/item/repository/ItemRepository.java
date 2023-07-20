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
            "WHERE UPPER(i.name) LIKE UPPER(CONCAT('%', :text, '%') ) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', :text, '%')) AND i.available = TRUE")
    List<Item> search(@Param("text") String text);
}
