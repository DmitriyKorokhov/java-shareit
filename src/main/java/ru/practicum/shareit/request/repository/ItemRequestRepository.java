package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

    List<ItemRequest> findRequestByRequestorIdOrderByCreatedDesc(int requestor);

    @Query("select r from requests r where r.requestor.id <> :user_id")
    Page<ItemRequest> findAllForUser(@Param("user_id") int userId, Pageable pageable);
}
