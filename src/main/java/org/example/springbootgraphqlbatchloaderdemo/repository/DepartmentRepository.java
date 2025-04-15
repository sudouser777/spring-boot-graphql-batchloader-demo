package org.example.springbootgraphqlbatchloaderdemo.repository;

import org.example.springbootgraphqlbatchloaderdemo.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByNameContainingIgnoreCase(String name);
    List<Department> findByLocationContainingIgnoreCase(String location);
    List<Department> findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(String name, String location);

    Page<Department> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Department> findByLocationContainingIgnoreCase(String location, Pageable pageable);
    Page<Department> findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(String name, String location, Pageable pageable);
}
