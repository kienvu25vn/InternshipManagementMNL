package com.quanlisinhvienmnl.repository;

import com.quanlisinhvienmnl.entity.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PositionRepository extends JpaRepository<Position , Long> {
    Position findByName(String name);

    Page<Position> findAllByDelAndNameContainingIgnoreCase(boolean del ,String name , Pageable pageable);

    List<Position> findAllByDel(boolean del);
}
