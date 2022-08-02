package com.quanlisinhvienmnl.repository;

import com.quanlisinhvienmnl.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University, Long> {
    University findByName(String name);
}
