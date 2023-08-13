package ru.practicum.shareit.permanentunits;

import org.springframework.data.domain.Sort;

public class Sorts {
    public static final Sort SORT_BY_START_DESC = Sort.by("start").descending();
    public static final Sort SORT_BY_CREATED_DESC = Sort.by("created").descending();
}
