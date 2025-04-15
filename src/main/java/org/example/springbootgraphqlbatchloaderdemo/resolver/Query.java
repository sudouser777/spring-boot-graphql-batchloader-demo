package org.example.springbootgraphqlbatchloaderdemo.resolver;

import org.example.springbootgraphqlbatchloaderdemo.model.Category;
import org.example.springbootgraphqlbatchloaderdemo.model.Department;
import org.example.springbootgraphqlbatchloaderdemo.model.Employee;
import org.example.springbootgraphqlbatchloaderdemo.model.Task;
import org.example.springbootgraphqlbatchloaderdemo.service.CategoryService;
import org.example.springbootgraphqlbatchloaderdemo.service.DepartmentService;
import org.example.springbootgraphqlbatchloaderdemo.service.EmployeeService;
import org.example.springbootgraphqlbatchloaderdemo.service.TaskService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class Query {
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;
    private final TaskService taskService;
    private final CategoryService categoryService;

    public Query(DepartmentService departmentService, EmployeeService employeeService, TaskService taskService, CategoryService categoryService) {
        this.departmentService = departmentService;
        this.employeeService = employeeService;
        this.taskService = taskService;
        this.categoryService = categoryService;
    }

    @QueryMapping
    public List<Department> departments(
            @Argument String name,
            @Argument String location,
            @Argument Integer page,
            @Argument Integer size) {
        return departmentService.getDepartments(name, location, page, size);
    }

    @QueryMapping
    public Department department(@Argument Long id) {
        return departmentService.getDepartmentById(id);
    }

    @QueryMapping
    public List<Employee> employees(
            @Argument String name,
            @Argument String position,
            @Argument Long departmentId,
            @Argument Integer page,
            @Argument Integer size) {
        return employeeService.getEmployees(name, position, departmentId, page, size);
    }

    @QueryMapping
    public Employee employee(@Argument Long id) {
        return employeeService.getEmployeeById(id);
    }

    @QueryMapping
    public List<Task> tasks(
            @Argument String title,
            @Argument String status,
            @Argument Long employeeId,
            @Argument Long categoryId,
            @Argument Integer page,
            @Argument Integer size) {
        return taskService.getTasks(title, status, employeeId, categoryId, page, size);
    }

    @QueryMapping
    public Task task(@Argument Long id) {
        return taskService.getTaskById(id);
    }

    @QueryMapping
    public List<Category> categories(
            @Argument String name,
            @Argument Integer page,
            @Argument Integer size) {
        return categoryService.getCategories(name, page, size);
    }

    @QueryMapping
    public Category category(@Argument Long id) {
        return categoryService.getCategoryById(id);
    }
}
