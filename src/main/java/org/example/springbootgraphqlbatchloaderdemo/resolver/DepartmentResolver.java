package org.example.springbootgraphqlbatchloaderdemo.resolver;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootgraphqlbatchloaderdemo.model.Department;
import org.example.springbootgraphqlbatchloaderdemo.model.Employee;
import org.example.springbootgraphqlbatchloaderdemo.service.EmployeeService;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class DepartmentResolver {

    private final EmployeeService employeeService;

    public DepartmentResolver(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @BatchMapping
    public Map<Department, List<Employee>> employees(List<Department> departments) {
        // Log that batch loading is happening
        log.info("Batch loading employees for {} departments: {}",
                departments.size(),
                departments.stream().map(d -> d.getId().toString()).collect(Collectors.joining(", ")));

        // Get all department IDs
        List<Long> departmentIds = departments.stream()
                .map(Department::getId)
                .collect(Collectors.toList());

        // Fetch employees for all departments in a single batch
        Map<Long, List<Employee>> employeesByDepartmentId = employeeService.getEmployeesByDepartmentIds(departmentIds);

        // Map Department objects to their employees
        return departments.stream()
                .collect(Collectors.toMap(
                    department -> department,
                    department -> employeesByDepartmentId.getOrDefault(department.getId(), List.of())
                ));
    }
}
