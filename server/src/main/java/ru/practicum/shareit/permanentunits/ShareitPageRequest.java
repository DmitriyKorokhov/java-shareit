package ru.practicum.shareit.permanentunits;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static ru.practicum.shareit.permanentunits.Constants.DEFAULT_FROM_INT;
import static ru.practicum.shareit.permanentunits.Constants.DEFAULT_SIZE_INT;

public class ShareitPageRequest extends PageRequest {

    public ShareitPageRequest() {
        this(Sort.unsorted());
    }

    public ShareitPageRequest(Sort sort) {
        this(DEFAULT_FROM_INT, DEFAULT_SIZE_INT, sort);
    }

    public ShareitPageRequest(int from, int size) {
        this(from, size, Sort.unsorted());
    }

    public ShareitPageRequest(int from, int size, Sort sort) {
        super(from / size, size, sort);
    }
}
