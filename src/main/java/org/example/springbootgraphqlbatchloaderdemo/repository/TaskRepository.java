package org.example.springbootgraphqlbatchloaderdemo.repository;

import org.example.springbootgraphqlbatchloaderdemo.model.Category;
import org.example.springbootgraphqlbatchloaderdemo.model.Employee;
import org.example.springbootgraphqlbatchloaderdemo.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Basic relationship queries
    List<Task> findByEmployee(Employee employee);
    List<Task> findByEmployeeIn(List<Employee> employees);
    List<Task> findByCategory(Category category);
    List<Task> findByCategoryIn(List<Category> categories);

    // Filtering queries
    List<Task> findByTitleContainingIgnoreCase(String title);
    List<Task> findByStatus(String status);
    List<Task> findByTitleContainingIgnoreCaseAndStatus(String title, String status);

    // Custom queries using @Query annotation
    @Query("SELECT t FROM Task t WHERE t.employee.id = :employeeId")
    List<Task> findByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT t FROM Task t WHERE t.category.id = :categoryId")
    List<Task> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT t FROM Task t WHERE t.employee.id = :employeeId AND t.category.id = :categoryId")
    List<Task> findByEmployeeIdAndCategoryId(
            @Param("employeeId") Long employeeId,
            @Param("categoryId") Long categoryId);

    @Query("SELECT t FROM Task t WHERE t.employee.id = :employeeId AND LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Task> findByEmployeeIdAndTitleContainingIgnoreCase(
            @Param("employeeId") Long employeeId,
            @Param("title") String title);

    @Query("SELECT t FROM Task t WHERE t.employee.id = :employeeId AND t.status = :status")
    List<Task> findByEmployeeIdAndStatus(
            @Param("employeeId") Long employeeId,
            @Param("status") String status);

    @Query("SELECT t FROM Task t WHERE t.category.id = :categoryId AND LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Task> findByCategoryIdAndTitleContainingIgnoreCase(
            @Param("categoryId") Long categoryId,
            @Param("title") String title);

    @Query("SELECT t FROM Task t WHERE t.category.id = :categoryId AND t.status = :status")
    List<Task> findByCategoryIdAndStatus(
            @Param("categoryId") Long categoryId,
            @Param("status") String status);

    // Paged queries
    Page<Task> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Task> findByStatus(String status, Pageable pageable);
    Page<Task> findByTitleContainingIgnoreCaseAndStatus(String title, String status, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.employee.id = :employeeId")
    Page<Task> findByEmployeeId(@Param("employeeId") Long employeeId, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.category.id = :categoryId")
    Page<Task> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.employee.id = :employeeId AND t.category.id = :categoryId")
    Page<Task> findByEmployeeIdAndCategoryId(
            @Param("employeeId") Long employeeId,
            @Param("categoryId") Long categoryId,
            Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.employee.id = :employeeId AND LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Task> findByEmployeeIdAndTitleContainingIgnoreCase(
            @Param("employeeId") Long employeeId,
            @Param("title") String title,
            Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.employee.id = :employeeId AND t.status = :status")
    Page<Task> findByEmployeeIdAndStatus(
            @Param("employeeId") Long employeeId,
            @Param("status") String status,
            Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.category.id = :categoryId AND LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Task> findByCategoryIdAndTitleContainingIgnoreCase(
            @Param("categoryId") Long categoryId,
            @Param("title") String title,
            Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.category.id = :categoryId AND t.status = :status")
    Page<Task> findByCategoryIdAndStatus(
            @Param("categoryId") Long categoryId,
            @Param("status") String status,
            Pageable pageable);
}
