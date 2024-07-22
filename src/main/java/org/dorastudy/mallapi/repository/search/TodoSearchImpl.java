package org.dorastudy.mallapi.repository.search;

import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.dorastudy.mallapi.domain.QTodo;
import org.dorastudy.mallapi.domain.Todo;
import org.dorastudy.mallapi.dto.PageRequestDTO;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch {
    public TodoSearchImpl() {
        super(Todo.class);
    }

    @Override
    public Page<Todo> search1(PageRequestDTO pageRequestDTO) {
        log.info("search1...................");
        QTodo todo = QTodo.todo; // query를 날리기 위한 객체

        JPQLQuery<Todo> query = from(todo);

        // paging 처리
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("tno").descending());

        this.getQuerydsl().applyPagination(pageable, query);

        List<Todo> list = query.fetch(); // query 실행, 목록 데이터 가져옴

        long total = query.fetchCount();

        return new PageImpl<>(list, pageable, total);
    }
}
