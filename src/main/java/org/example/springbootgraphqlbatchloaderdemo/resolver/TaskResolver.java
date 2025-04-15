package org.example.springbootgraphqlbatchloaderdemo.resolver;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootgraphqlbatchloaderdemo.model.Category;
import org.example.springbootgraphqlbatchloaderdemo.model.Employee;
import org.example.springbootgraphqlbatchloaderdemo.model.Task;
import org.example.springbootgraphqlbatchloaderdemo.service.CategoryService;
import org.example.springbootgraphqlbatchloaderdemo.service.EmployeeService;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class TaskResolver {

    private final EmployeeService employeeService;
    private final CategoryService categoryService;

    public TaskResolver(EmployeeService employeeService, CategoryService categoryService) {
        this.employeeService = employeeService;
        this.categoryService = categoryService;
    }

    @SchemaMapping(typeName = "Task", field = "employee")
    public Employee getEmployee(Task task) {
        return employeeService.getEmployeeById(task.getEmployeeId());
    }

    @BatchMapping
    public Map<Task, Category> category(List<Task> tasks) {
        // Log that batch loading is happening
        log.info("Batch loading categories for {} tasks: {}",
                tasks.size(),
                tasks.stream().map(t -> t.getId().toString()).collect(Collectors.joining(", ")));

        // Get all category IDs
        List<Long> categoryIds = tasks.stream()
                .map(Task::getCategoryId)
                .distinct()
                .collect(Collectors.toList());

        // Fetch categories for all tasks in a single batch
        List<Category> categories = categoryService.getCategoriesByIds(categoryIds);
        Map<Long, Category> categoryMap = categories.stream()
                .collect(Collectors.toMap(Category::getId, category -> category));

        // Map Task objects to their categories
        return tasks.stream()
                .collect(Collectors.toMap(
                    task -> task,
                    task -> categoryMap.get(task.getCategoryId())
                ));
    }
}
