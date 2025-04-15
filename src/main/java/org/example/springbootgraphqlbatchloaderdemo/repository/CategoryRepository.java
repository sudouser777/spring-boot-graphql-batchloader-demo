package org.example.springbootgraphqlbatchloaderdemo.repository;

import org.example.springbootgraphqlbatchloaderdemo.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Filtering queries
    List<Category> findByNameContainingIgnoreCase(String name);

    // Paged queries
    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
