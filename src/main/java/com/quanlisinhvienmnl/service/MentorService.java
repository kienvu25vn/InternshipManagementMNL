package com.quanlisinhvienmnl.service;

import com.quanlisinhvienmnl.entity.Users;
import org.springframework.ui.Model;

public interface MentorService {

    void Mentor(Model model , String search , String sort , String orderby , Integer page , Integer size);

    void addMentorView(Model model);

    void addMentor(Model model , Users users , String position);

    void updateMentorView(Model model , Long id );

    void updateMentor(Model model , Long id , Users users , String position);

    void deleteMentor(Long id);

    void profileMentor(Model model , Long id);
}
