package org.example.springbootgraphqlbatchloaderdemo.service;

import org.example.springbootgraphqlbatchloaderdemo.model.Department;
import org.example.springbootgraphqlbatchloaderdemo.repository.DepartmentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public List<Department> getDepartmentsByIds(List<Long> ids) {
        return departmentRepository.findAllById(ids);
    }

    public List<Department> getDepartments(String name, String location, Integer page, Integer size) {
        // If pagination is requested
        if (page != null && size != null) {
            PageRequest pageRequest = PageRequest.of(page, size);

            // Apply filters if provided
            if (name != null && location != null) {
                return departmentRepository.findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(name, location, pageRequest).getContent();
            } else if (name != null) {
                return departmentRepository.findByNameContainingIgnoreCase(name, pageRequest).getContent();
            } else if (location != null) {
                return departmentRepository.findByLocationContainingIgnoreCase(location, pageRequest).getContent();
            } else {
                return departmentRepository.findAll(pageRequest).getContent();
            }
        }
        // No pagination
        else {
            // Apply filters if provided
            if (name != null && location != null) {
                return departmentRepository.findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(name, location);
            } else if (name != null) {
                return departmentRepository.findByNameContainingIgnoreCase(name);
            } else if (location != null) {
                return departmentRepository.findByLocationContainingIgnoreCase(location);
            } else {
                return departmentRepository.findAll();
            }
        }
    }
}
