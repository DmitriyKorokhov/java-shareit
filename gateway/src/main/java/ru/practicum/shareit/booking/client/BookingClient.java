package ru.practicum.shareit.booking.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
@Slf4j
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addBooking(BookingRequestDto bookingIncomeDto, int userId) {
        log.info("Обработка запроса создания аренды от пользователя c id = {} ", userId);
        return post("", userId, bookingIncomeDto);
    }


    public ResponseEntity<Object> approveBooking(int bookingId, int userId, boolean approved) {
        Map<String, Object> parameters = Map.of("approved", approved);
        return patch("/" + bookingId + "?approved={approved}", userId, parameters, null);
    }

    public ResponseEntity<Object> getBookingById(int bookingId, int userId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllBookings(int userId, BookingState state, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size,
                "state", state.name()
        );
        return get("?from={from}&size={size}&state={state}", userId, parameters);
    }


    public ResponseEntity<Object> getAllBookingForOwner(int userId, BookingState state, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size,
                "state", state.name()
        );
        return get("/owner?from={from}&size={size}&state={state}", userId, parameters);
    }
}