package com.quanlisinhvienmnl.service;

import com.quanlisinhvienmnl.entity.Internship;
import com.quanlisinhvienmnl.model.UserInternship;
import org.springframework.ui.Model;

import java.text.ParseException;
import java.util.List;

public interface InternshipService {
    List<Internship> findAll();

    Internship findByUserId(Long id);

    void save(Internship internship);

    void deleteByUserId(Long id);

    Internship findById(Long id);

    void Internship(Model model , String search , String sort , String orderby , Integer page , Integer size);

    void addNewInternship(Model model  , String university , String position , String birthday , UserInternship userInternship) throws ParseException;

    void updateInternshipView(Long id , Model model);

    void updateInternship(Model model  ,Long id , String university , String position , String birthday , UserInternship userInternship) throws ParseException;

    void internshipProfile(Long id , Model model);

    void internshipAssignment(Long userId , Long mentorId);

    void internshipReview(Long id , String reviewer , String content);

    void internshipError(Model model);
}
