package org.dorastudy.mallapi.repository;

import lombok.extern.log4j.Log4j2;
import org.dorastudy.mallapi.domain.Todo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class TodoRepositoryTests {
    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void test1() {
        Assertions.assertNotNull(todoRepository);
        log.info(todoRepository.getClass().getName());
    }

    @Test
    public void testInsert() {
        final Todo todo = Todo.builder()
                .title("Title")
                .content("Content...")
                .dueDate(LocalDate.of(2023, 12, 30))
                .build();

        Todo result = todoRepository.save(todo);
        log.info(result);
    }

    @Test
    public void testRead() {
        Long tno = 1L;

        Optional<Todo> result = todoRepository.findById(tno);

        Todo todo = result.orElseThrow();

        log.info(todo);
    }

    @Test
    public void testUpdate() {
        // 로딩 후 엔티티 객체 변경 (setter 사용)
        Long tno = 1L;

        Optional<Todo> result = todoRepository.findById(tno);

        Todo todo = result.orElseThrow();

        todo.changeTitle("Update Title");
        todo.changeContent("Updated Content");
        todo.changeComplete(true);

        todoRepository.save(todo);
    }
}
