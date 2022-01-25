package com.example.spring1.repository;

import com.example.spring1.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long > {
    public Optional<Department> findByName(String name);
}
