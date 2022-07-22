package com.quanlisinhvienmnl.service;

import com.quanlisinhvienmnl.entity.Internship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InternshipService {
    List<Internship> findAll();

    Internship findByUserId(Long id);

    void save(Internship internship);

    void deleteByUserId(Long id);

}
