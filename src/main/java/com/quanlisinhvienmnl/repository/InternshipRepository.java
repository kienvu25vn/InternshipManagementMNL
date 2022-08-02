package com.quanlisinhvienmnl.repository;

import com.quanlisinhvienmnl.entity.Internship;
import com.quanlisinhvienmnl.entity.Mentor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternshipRepository extends JpaRepository<Internship, Long> {

    Page<Internship> findAllByStatus( int status , Pageable pageable);

    Page<Internship> findAllByUseridAndStatus (Long userid , int status , Pageable pageable);

    Page<Internship> findAllByStatusAndMentorsContains( int status , Mentor mentor , Pageable pageable);

    Page<Internship> findAllByStatusAndMentorsContainsAndUseridIn( int status , Mentor mentor ,List<Long> userids , Pageable pageable);

    List<Internship> findAllByStatusAndMentorsContains(int status , Mentor mentor);
    List<Internship> findAllByStatus(int status);
    Internship findByUserid(Long id);


}
