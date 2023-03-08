package io.github.maxixcom.otus.movies.backend.mappers;

import io.github.maxixcom.otus.movies.backend.dto.MetaDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PageMapper {
    @Mapping(source = "number", target = "page")
    MetaDto pageToMeta(Page page);
}
