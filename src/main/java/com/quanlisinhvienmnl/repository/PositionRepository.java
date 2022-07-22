package com.quanlisinhvienmnl.repository;

import com.quanlisinhvienmnl.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position , Long> {
    Position findByName(String name);
}
