package com.quanlisinhvienmnl.repository;

import com.quanlisinhvienmnl.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorRepository extends JpaRepository<Mentor , Long> {

    List<Mentor> findAllByUserid(Long userid);

    Mentor findByUserid(Long id);

    List<Mentor> findAllByMaxInternshipGreaterThanAndDel(int maxInternship , boolean del);




}
