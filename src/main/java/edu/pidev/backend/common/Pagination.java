package edu.pidev.backend.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pagination {
    int number;
    int size;
    long totalElements;
    int totalPages;
    boolean first;
    boolean last;
}
