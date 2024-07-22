package org.dorastudy.mallapi.repository.search;

import org.dorastudy.mallapi.domain.Todo;
import org.dorastudy.mallapi.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface TodoSearch {
    Page<Todo> search1(PageRequestDTO pageRequestDTO);
}
