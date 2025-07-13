package com.ahmed.taskmanager.repository;

import com.ahmed.taskmanager.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}