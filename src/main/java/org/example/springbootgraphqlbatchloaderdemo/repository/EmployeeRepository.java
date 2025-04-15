package org.example.springbootgraphqlbatchloaderdemo.repository;

import org.example.springbootgraphqlbatchloaderdemo.model.Department;
import org.example.springbootgraphqlbatchloaderdemo.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Basic relationship queries
    List<Employee> findByDepartment(Department department);
    List<Employee> findByDepartmentIn(List<Department> departments);

    // Filtering queries
    List<Employee> findByNameContainingIgnoreCase(String name);
    List<Employee> findByPositionContainingIgnoreCase(String position);
    List<Employee> findByNameContainingIgnoreCaseAndPositionContainingIgnoreCase(String name, String position);

    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId AND LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Employee> findByDepartmentIdAndNameContainingIgnoreCase(
            @Param("departmentId") Long departmentId,
            @Param("name") String name);

    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId AND LOWER(e.position) LIKE LOWER(CONCAT('%', :position, '%'))")
    List<Employee> findByDepartmentIdAndPositionContainingIgnoreCase(
            @Param("departmentId") Long departmentId,
            @Param("position") String position);

    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId AND LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')) AND LOWER(e.position) LIKE LOWER(CONCAT('%', :position, '%'))")
    List<Employee> findByDepartmentIdAndNameContainingIgnoreCaseAndPositionContainingIgnoreCase(
            @Param("departmentId") Long departmentId,
            @Param("name") String name,
            @Param("position") String position);

    // Paged queries
    Page<Employee> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Employee> findByPositionContainingIgnoreCase(String position, Pageable pageable);
    Page<Employee> findByNameContainingIgnoreCaseAndPositionContainingIgnoreCase(String name, String position, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId")
    Page<Employee> findByDepartmentId(@Param("departmentId") Long departmentId, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId AND LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Employee> findByDepartmentIdAndNameContainingIgnoreCase(
            @Param("departmentId") Long departmentId,
            @Param("name") String name,
            Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId AND LOWER(e.position) LIKE LOWER(CONCAT('%', :position, '%'))")
    Page<Employee> findByDepartmentIdAndPositionContainingIgnoreCase(
            @Param("departmentId") Long departmentId,
            @Param("position") String position,
            Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId AND LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')) AND LOWER(e.position) LIKE LOWER(CONCAT('%', :position, '%'))")
    Page<Employee> findByDepartmentIdAndNameContainingIgnoreCaseAndPositionContainingIgnoreCase(
            @Param("departmentId") Long departmentId,
            @Param("name") String name,
            @Param("position") String position,
            Pageable pageable);
}
