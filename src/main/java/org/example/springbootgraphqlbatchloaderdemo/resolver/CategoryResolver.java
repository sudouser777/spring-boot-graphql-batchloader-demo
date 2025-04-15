package org.example.springbootgraphqlbatchloaderdemo.resolver;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootgraphqlbatchloaderdemo.model.Category;
import org.example.springbootgraphqlbatchloaderdemo.model.Task;
import org.example.springbootgraphqlbatchloaderdemo.service.TaskService;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class CategoryResolver {

    private final TaskService taskService;

    public CategoryResolver(TaskService taskService) {
        this.taskService = taskService;
    }

    @BatchMapping
    public Map<Category, List<Task>> tasks(List<Category> categories) {
        // Log that batch loading is happening
        log.info("Batch loading tasks for {} categories: {}",
                categories.size(),
                categories.stream().map(c -> c.getId().toString()).collect(Collectors.joining(", ")));

        // Get all category IDs
        List<Long> categoryIds = categories.stream()
                .map(Category::getId)
                .collect(Collectors.toList());

        // Fetch tasks for all categories in a single batch
        Map<Long, List<Task>> tasksByCategoryId = taskService.getTasksByCategoryIds(categoryIds);

        // Map Category objects to their tasks
        return categories.stream()
                .collect(Collectors.toMap(
                    category -> category,
                    category -> tasksByCategoryId.getOrDefault(category.getId(), List.of())
                ));
    }
}
