package com.quanlisinhvienmnl.repository;

import com.quanlisinhvienmnl.entity.Internship;
import com.quanlisinhvienmnl.entity.InternshipTimesheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TimesheetRepository extends JpaRepository<InternshipTimesheet , Long> {
    List<InternshipTimesheet> findAllByInternshipAndDelAndWorkingDayGreaterThanEqualAndWorkingDayLessThanEqual(Internship internship ,boolean del ,Date startDay , Date endDay);

    InternshipTimesheet findByInternshipAndWorkingDayAndDel(Internship internship ,Date day , boolean del);


}
