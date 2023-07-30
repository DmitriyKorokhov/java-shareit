package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("select b from bookings b " +
            "where b.booker.id = :user_id " +
            "and b.start < :time " +
            "and b.end > :time")
    Page<Booking> findByBookerIdCurrent(@Param("user_id") int userId, @Param("time") LocalDateTime now, Pageable page);

    Page<Booking> findByBookerIdAndEndIsBefore(int userId, LocalDateTime now, Pageable page);

    Page<Booking> findByBookerIdAndStartIsAfter(int userId, LocalDateTime now, Pageable page);

    Page<Booking> findByBookerIdAndStatus(int userId, BookingStatus bookingStatus, Pageable page);

    @Query("select b " +
            "from bookings b " +
            "where b.item.owner = :user " +
            "and b.start < :time " +
            "and b.end > :time")
    Page<Booking> findBookingsByItemOwnerCurrent(@Param("user") User owner, @Param("time") LocalDateTime now, Pageable page);

    Page<Booking> findBookingByItemOwnerAndEndIsBefore(User owner, LocalDateTime now, Pageable page);

    Page<Booking> findBookingByItemOwnerAndStartIsAfter(User owner, LocalDateTime now, Pageable page);

    Page<Booking> findBookingByItemOwnerAndStatus(User owner, BookingStatus status, Pageable page);

    Page<Booking> findBookingByItemOwner(User owner, Pageable page);

    Booking findFirstBookingByItemIdAndStartBeforeAndStatusOrderByStartDesc(int itemId, LocalDateTime now, BookingStatus status);

    Booking findFirstBookingByItemIdAndStartAfterAndStatusOrderByStartAsc(int itemId, LocalDateTime now, BookingStatus status);

    Page<Booking> findByBookerId(int userId, Pageable page);

    List<Booking> findBookingByItemIdAndBookerIdAndStatusAndStartBefore(int itemId, int bookerId, BookingStatus status, LocalDateTime now);

    List<Booking> findBookingByItemInAndStatus(Collection<Item> items, BookingStatus status);
}