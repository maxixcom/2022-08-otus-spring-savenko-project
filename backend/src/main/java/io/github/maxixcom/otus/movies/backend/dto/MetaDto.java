package io.github.maxixcom.otus.movies.backend.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class MetaDto {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public static <T> MetaDto fromPage(Page<T> page) {
        MetaDto metaDto = new MetaDto();
        metaDto.setPage(page.getNumber());
        metaDto.setSize(page.getSize());
        metaDto.setTotalElements(page.getTotalElements());
        metaDto.setTotalPages(page.getTotalPages());
        return metaDto;
    }
}
