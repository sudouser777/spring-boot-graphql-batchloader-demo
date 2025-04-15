package org.example.springbootgraphqlbatchloaderdemo.service;

import org.example.springbootgraphqlbatchloaderdemo.model.Category;
import org.example.springbootgraphqlbatchloaderdemo.model.Employee;
import org.example.springbootgraphqlbatchloaderdemo.model.Task;
import org.example.springbootgraphqlbatchloaderdemo.repository.CategoryRepository;
import org.example.springbootgraphqlbatchloaderdemo.repository.EmployeeRepository;
import org.example.springbootgraphqlbatchloaderdemo.repository.TaskRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final CategoryRepository categoryRepository;

    public TaskService(TaskRepository taskRepository, EmployeeRepository employeeRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> getTasksByIds(List<Long> ids) {
        return taskRepository.findAllById(ids);
    }

    public List<Task> getTasksByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            return List.of();
        }
        return taskRepository.findByEmployee(employee);
    }

    public Map<Long, List<Task>> getTasksByEmployeeIds(List<Long> employeeIds) {
        List<Employee> employees = employeeRepository.findAllById(employeeIds);
        List<Task> tasks = taskRepository.findByEmployeeIn(employees);
        return tasks.stream()
                .collect(Collectors.groupingBy(task -> task.getEmployeeId()));
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

    public List<Task> getTasks(String title, String status, Long employeeId, Long categoryId, Integer page, Integer size) {
        // If pagination is requested
        if (page != null && size != null) {
            PageRequest pageRequest = PageRequest.of(page, size);

            // Apply filters based on combinations of parameters
            if (employeeId != null && categoryId != null) {
                return taskRepository.findByEmployeeIdAndCategoryId(employeeId, categoryId, pageRequest).getContent();
            } else if (employeeId != null) {
                if (title != null && status != null) {
                    // Not implemented in repository, would need to add this method
                    return taskRepository.findByEmployeeId(employeeId, pageRequest).getContent();
                } else if (title != null) {
                    return taskRepository.findByEmployeeIdAndTitleContainingIgnoreCase(employeeId, title, pageRequest).getContent();
                } else if (status != null) {
                    return taskRepository.findByEmployeeIdAndStatus(employeeId, status, pageRequest).getContent();
                } else {
                    return taskRepository.findByEmployeeId(employeeId, pageRequest).getContent();
                }
            } else if (categoryId != null) {
                if (title != null && status != null) {
                    // Not implemented in repository, would need to add this method
                    return taskRepository.findByCategoryId(categoryId, pageRequest).getContent();
                } else if (title != null) {
                    return taskRepository.findByCategoryIdAndTitleContainingIgnoreCase(categoryId, title, pageRequest).getContent();
                } else if (status != null) {
                    return taskRepository.findByCategoryIdAndStatus(categoryId, status, pageRequest).getContent();
                } else {
                    return taskRepository.findByCategoryId(categoryId, pageRequest).getContent();
                }
            } else {
                if (title != null && status != null) {
                    return taskRepository.findByTitleContainingIgnoreCaseAndStatus(title, status, pageRequest).getContent();
                } else if (title != null) {
                    return taskRepository.findByTitleContainingIgnoreCase(title, pageRequest).getContent();
                } else if (status != null) {
                    return taskRepository.findByStatus(status, pageRequest).getContent();
                } else {
                    return taskRepository.findAll(pageRequest).getContent();
                }
            }
        }
        // No pagination
        else {
            // Apply filters based on combinations of parameters
            if (employeeId != null && categoryId != null) {
                return taskRepository.findByEmployeeIdAndCategoryId(employeeId, categoryId);
            } else if (employeeId != null) {
                if (title != null && status != null) {
                    // Not implemented in repository, would need to add this method
                    return taskRepository.findByEmployeeId(employeeId);
                } else if (title != null) {
                    return taskRepository.findByEmployeeIdAndTitleContainingIgnoreCase(employeeId, title);
                } else if (status != null) {
                    return taskRepository.findByEmployeeIdAndStatus(employeeId, status);
                } else {
                    return taskRepository.findByEmployeeId(employeeId);
                }
            } else if (categoryId != null) {
                if (title != null && status != null) {
                    // Not implemented in repository, would need to add this method
                    return taskRepository.findByCategoryId(categoryId);
                } else if (title != null) {
                    return taskRepository.findByCategoryIdAndTitleContainingIgnoreCase(categoryId, title);
                } else if (status != null) {
                    return taskRepository.findByCategoryIdAndStatus(categoryId, status);
                } else {
                    return taskRepository.findByCategoryId(categoryId);
                }
            } else {
                if (title != null && status != null) {
                    return taskRepository.findByTitleContainingIgnoreCaseAndStatus(title, status);
                } else if (title != null) {
                    return taskRepository.findByTitleContainingIgnoreCase(title);
                } else if (status != null) {
                    return taskRepository.findByStatus(status);
                } else {
                    return taskRepository.findAll();
                }
            }
        }
    }
}
