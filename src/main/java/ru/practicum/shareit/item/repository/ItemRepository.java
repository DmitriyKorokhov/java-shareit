package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Page<Item> findAllByOwnerOrderById(User user, Pageable page);

    @Query("SELECT i FROM items i " +
            "WHERE UPPER(i.name) LIKE UPPER(CONCAT('%', :text, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', :text, '%')) " +
            "AND i.available = TRUE")
    List<Item> search(@Param("text") String text, Pageable page);

    List<Item> findAllByItemRequest(ItemRequest request);

    @Query("SELECT it FROM items AS it " +
            "WHERE it.itemRequest IN :requests")
    List<Item> findAllByRequestIdIn(@Param("requests") List<ItemRequest> requests);
}