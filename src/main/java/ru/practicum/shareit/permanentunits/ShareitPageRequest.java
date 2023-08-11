package ru.practicum.shareit.permanentunits;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class ShareitPageRequest extends PageRequest {

    public ShareitPageRequest() {
        this(Sort.unsorted());
    }

    public ShareitPageRequest(Sort sort) {
        this(0, 20, sort);
    }

    public ShareitPageRequest(int from, int size) {
        this(from, size, Sort.unsorted());
    }

    public ShareitPageRequest(int from, int size, Sort sort) {
        super(from / size, size, sort);
    }
}
