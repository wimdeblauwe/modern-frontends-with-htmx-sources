package com.modernfrontendshtmx.todomvc.todo;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface SpringDataJpaTodoItemRepository extends ListCrudRepository<TodoItem, Long> {
    int countAllByCompleted(boolean completed);

    List<TodoItem> findAllByCompleted(boolean completed);
}
