package com.quanlisinhvienmnl.service;

import com.quanlisinhvienmnl.entity.Internship;
import com.quanlisinhvienmnl.entity.InternshipTimesheet;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface TimesheetService {
    List<InternshipTimesheet> findAllInWeek(Internship internship , boolean del ,Date start , Date end);

    InternshipTimesheet findByDay(Internship internship ,Date day , boolean del);

    void InternshipTimesheet(Model model , LocalDate instanceDay , String status ,String search , Long id) throws ParseException;

    void addTimesheetView(Model model , Long id) throws ParseException;

    void addTimesheet(String workDate , String workTime , Long id) throws ParseException;

    void deleteTimesheetById(Long id);

    void updateTimesheetView(Model model , Long id , String workDay) throws ParseException;

    void updateTimesheet(Long id , String workTime , String  workDate) throws ParseException;

}
