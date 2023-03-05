package io.github.maxixcom.otus.movies.backend.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
//@Data
public abstract class ListDto<T> {
    protected List<T> items = Collections.emptyList();
    protected MetaDto meta = new MetaDto();

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public MetaDto getMeta() {
        return meta;
    }

    public void setMeta(MetaDto meta) {
        this.meta = meta;
    }
}
