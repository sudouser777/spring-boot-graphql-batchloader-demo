package org.example.springbootgraphqlbatchloaderdemo.service;

import org.example.springbootgraphqlbatchloaderdemo.model.Department;
import org.example.springbootgraphqlbatchloaderdemo.model.Employee;
import org.example.springbootgraphqlbatchloaderdemo.repository.DepartmentRepository;
import org.example.springbootgraphqlbatchloaderdemo.repository.EmployeeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public List<Employee> getEmployeesByIds(List<Long> ids) {
        return employeeRepository.findAllById(ids);
    }

    public List<Employee> getEmployeesByDepartmentId(Long departmentId) {
        Department department = departmentRepository.findById(departmentId).orElse(null);
        if (department == null) {
            return List.of();
        }
        return employeeRepository.findByDepartment(department);
    }

    public Map<Long, List<Employee>> getEmployeesByDepartmentIds(List<Long> departmentIds) {
        List<Department> departments = departmentRepository.findAllById(departmentIds);
        List<Employee> employees = employeeRepository.findByDepartmentIn(departments);
        return employees.stream()
                .collect(Collectors.groupingBy(employee -> employee.getDepartmentId()));
    }

    public List<Employee> getEmployees(String name, String position, Long departmentId, Integer page, Integer size) {
        // If pagination is requested
        if (page != null && size != null) {
            PageRequest pageRequest = PageRequest.of(page, size);

            // Apply filters based on combinations of parameters
            if (departmentId != null) {
                if (name != null && position != null) {
                    return employeeRepository.findByDepartmentIdAndNameContainingIgnoreCaseAndPositionContainingIgnoreCase(
                            departmentId, name, position, pageRequest).getContent();
                } else if (name != null) {
                    return employeeRepository.findByDepartmentIdAndNameContainingIgnoreCase(
                            departmentId, name, pageRequest).getContent();
                } else if (position != null) {
                    return employeeRepository.findByDepartmentIdAndPositionContainingIgnoreCase(
                            departmentId, position, pageRequest).getContent();
                } else {
                    return employeeRepository.findByDepartmentId(departmentId, pageRequest).getContent();
                }
            } else {
                if (name != null && position != null) {
                    return employeeRepository.findByNameContainingIgnoreCaseAndPositionContainingIgnoreCase(
                            name, position, pageRequest).getContent();
                } else if (name != null) {
                    return employeeRepository.findByNameContainingIgnoreCase(name, pageRequest).getContent();
                } else if (position != null) {
                    return employeeRepository.findByPositionContainingIgnoreCase(position, pageRequest).getContent();
                } else {
                    return employeeRepository.findAll(pageRequest).getContent();
                }
            }
        }
        // No pagination
        else {
            // Apply filters based on combinations of parameters
            if (departmentId != null) {
                Department department = departmentRepository.findById(departmentId).orElse(null);
                if (department == null) {
                    return List.of();
                }

                if (name != null && position != null) {
                    return employeeRepository.findByDepartmentIdAndNameContainingIgnoreCaseAndPositionContainingIgnoreCase(
                            departmentId, name, position);
                } else if (name != null) {
                    return employeeRepository.findByDepartmentIdAndNameContainingIgnoreCase(departmentId, name);
                } else if (position != null) {
                    return employeeRepository.findByDepartmentIdAndPositionContainingIgnoreCase(departmentId, position);
                } else {
                    return employeeRepository.findByDepartment(department);
                }
            } else {
                if (name != null && position != null) {
                    return employeeRepository.findByNameContainingIgnoreCaseAndPositionContainingIgnoreCase(name, position);
                } else if (name != null) {
                    return employeeRepository.findByNameContainingIgnoreCase(name);
                } else if (position != null) {
                    return employeeRepository.findByPositionContainingIgnoreCase(position);
                } else {
                    return employeeRepository.findAll();
                }
            }
        }
    }
}
