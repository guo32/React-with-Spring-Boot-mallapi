package org.dorastudy.mallapi.repository;

import org.dorastudy.mallapi.domain.Todo;
import org.dorastudy.mallapi.repository.search.TodoSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoSearch {
}
