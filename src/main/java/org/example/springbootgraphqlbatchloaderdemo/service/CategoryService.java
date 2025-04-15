package org.example.springbootgraphqlbatchloaderdemo.service;

import org.example.springbootgraphqlbatchloaderdemo.model.Category;
import org.example.springbootgraphqlbatchloaderdemo.model.Task;
import org.example.springbootgraphqlbatchloaderdemo.repository.CategoryRepository;
import org.example.springbootgraphqlbatchloaderdemo.repository.TaskRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;

    public CategoryService(CategoryRepository categoryRepository, TaskRepository taskRepository) {
        this.categoryRepository = categoryRepository;
        this.taskRepository = taskRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public List<Category> getCategoriesByIds(List<Long> ids) {
        return categoryRepository.findAllById(ids);
    }

    public List<Task> getTasksByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return List.of();
        }
        return taskRepository.findByCategory(category);
    }

    public Map<Long, List<Task>> getTasksByCategoryIds(List<Long> categoryIds) {
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        List<Task> tasks = taskRepository.findByCategoryIn(categories);
        return tasks.stream()
                .collect(Collectors.groupingBy(task -> task.getCategoryId()));
    }

    public List<Category> getCategories(String name, Integer page, Integer size) {
        // If pagination is requested
        if (page != null && size != null) {
            PageRequest pageRequest = PageRequest.of(page, size);

            // Apply filters if provided
            if (name != null) {
                return categoryRepository.findByNameContainingIgnoreCase(name, pageRequest).getContent();
            } else {
                return categoryRepository.findAll(pageRequest).getContent();
            }
        }
        // No pagination
        else {
            // Apply filters if provided
            if (name != null) {
                return categoryRepository.findByNameContainingIgnoreCase(name);
            } else {
                return categoryRepository.findAll();
            }
        }
    }
}
