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

    @Query(" select i from items i " +
            "where upper(i.name) like upper(concat('%', :text, '%')) " +
            " or upper(i.description) like upper(concat('%', :text, '%')) and i.available = true")
    List<Item> search(@Param("text") String text, Pageable page);

    List<Item> findAllByItemRequest(ItemRequest request);

    @Query("select it from items as it where it.itemRequest in ?1")
    List<Item> findAllByRequestIdIn(List<ItemRequest> requests);
}