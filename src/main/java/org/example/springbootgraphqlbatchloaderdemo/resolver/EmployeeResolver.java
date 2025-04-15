package org.example.springbootgraphqlbatchloaderdemo.resolver;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootgraphqlbatchloaderdemo.model.Department;
import org.example.springbootgraphqlbatchloaderdemo.model.Employee;
import org.example.springbootgraphqlbatchloaderdemo.model.Task;
import org.example.springbootgraphqlbatchloaderdemo.service.DepartmentService;
import org.example.springbootgraphqlbatchloaderdemo.service.TaskService;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class EmployeeResolver {

    private final DepartmentService departmentService;
    private final TaskService taskService;

    public EmployeeResolver(DepartmentService departmentService, TaskService taskService) {
        this.departmentService = departmentService;
        this.taskService = taskService;
    }

    @SchemaMapping(typeName = "Employee", field = "department")
    public Department getDepartment(Employee employee) {
        return departmentService.getDepartmentById(employee.getDepartmentId());
    }

    @BatchMapping
    public Map<Employee, List<Task>> tasks(List<Employee> employees) {
        // Log that batch loading is happening
        log.info("Batch loading tasks for {} employees: {}",
                employees.size(),
                employees.stream().map(e -> e.getId().toString()).collect(Collectors.joining(", ")));

        // Get all employee IDs
        List<Long> employeeIds = employees.stream()
                .map(Employee::getId)
                .collect(Collectors.toList());

        // Fetch tasks for all employees in a single batch
        Map<Long, List<Task>> tasksByEmployeeId = taskService.getTasksByEmployeeIds(employeeIds);

        // Map Employee objects to their tasks
        return employees.stream()
                .collect(Collectors.toMap(
                    employee -> employee,
                    employee -> tasksByEmployeeId.getOrDefault(employee.getId(), List.of())
                ));
    }
}
