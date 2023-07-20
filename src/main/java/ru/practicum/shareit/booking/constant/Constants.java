package ru.practicum.shareit.booking.constant;

import org.springframework.data.domain.Sort;

public class Constants {
    public static final String PATTERN_DATE = "yyyy-MM-dd HH:mm";

    public static final Sort SORT_BY_START_DESC = Sort.by("start").descending();

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
}
