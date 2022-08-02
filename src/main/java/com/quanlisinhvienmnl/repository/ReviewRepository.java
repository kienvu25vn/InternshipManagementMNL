package com.quanlisinhvienmnl.repository;

import com.quanlisinhvienmnl.entity.Internship;
import com.quanlisinhvienmnl.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Reviews , Long> {

    List<Reviews> findAllByInternship(Internship internship);
}
