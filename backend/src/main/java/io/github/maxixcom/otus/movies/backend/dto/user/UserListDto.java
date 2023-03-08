
package io.github.maxixcom.otus.movies.backend.dto.user;

import io.github.maxixcom.otus.movies.backend.dto.ListDto;
import io.github.maxixcom.otus.movies.backend.dto.MetaDto;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class UserListDto extends ListDto<UserDto> {
    public UserListDto(List<UserDto> items, MetaDto meta) {
        super(items, meta);
    }
}
