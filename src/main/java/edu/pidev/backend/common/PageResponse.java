package edu.pidev.backend.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class PageResponse<T> {
    private List<T> data;
    private Payload payload;

    public PageResponse(List<T> objects, Pagination pagination) {
        this.data = objects;
        this.payload = new Payload(pagination);
    }
}
