package com.modernfrontendshtmx.todomvc.todo;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TodoItemRepository {
    private final SpringDataJpaTodoItemRepository repository;

    public TodoItemRepository(SpringDataJpaTodoItemRepository repository) {
        this.repository = repository;
    }

    public TodoItem save(TodoItem todoItem) {
        return repository.save(todoItem);
    }

    public Optional<TodoItem> findById(Long id) {
        return repository.findById(id);
    }

    public List<TodoItem> findAll() {
        return repository.findAll();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public long count() {
        return repository.count();
    }

    public int countAllByCompleted(boolean completed) {
        return repository.countAllByCompleted(completed);
    }

    public List<TodoItem> findAllByCompleted(boolean completed) {
        return repository.findAllByCompleted(completed);
    }
}
