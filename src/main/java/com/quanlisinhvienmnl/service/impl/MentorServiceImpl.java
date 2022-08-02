package com.quanlisinhvienmnl.service.impl;

import com.quanlisinhvienmnl.countfunction.Function;
import com.quanlisinhvienmnl.entity.Internship;
import com.quanlisinhvienmnl.entity.Mentor;
import com.quanlisinhvienmnl.entity.Position;
import com.quanlisinhvienmnl.entity.Users;
import com.quanlisinhvienmnl.repository.InternshipRepository;
import com.quanlisinhvienmnl.repository.MentorRepository;
import com.quanlisinhvienmnl.repository.PositionRepository;
import com.quanlisinhvienmnl.repository.UserRepository;
import com.quanlisinhvienmnl.service.InternshipService;
import com.quanlisinhvienmnl.service.MentorService;
import com.quanlisinhvienmnl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SessionAttributes("link")
@Service
public class MentorServiceImpl implements MentorService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InternshipRepository internshipRepository;

    @Autowired
    private InternshipService internshipService;

    @Override
    public void Mentor(Model model, String search, String sort, String orderby, Integer page, Integer size) {
        if(search == null)
            search = "";
        int total = 0;
        Function function = new Function();
        function.role(model , userService);
        Map<Users, Mentor> map = new HashMap<>();

        Pageable pageable = function.pageable(page , size , orderby , sort);
        Page<Users> users = userRepository.findAllByRoleAndDelAndAndFullnameContainsIgnoreCase("ROLE_MENTOR" , false , search , pageable);
        total = (int) users.getTotalElements();

        List<Mentor> mentors = new ArrayList<>();
        for(Users us : users){
            mentors.add(mentorRepository.findByUserid(us.getId()));
        }
        for(int i = 0 ;i<mentors.size();i++){
            map.put(users.getContent().get(i),mentors.get(i) );
        }

        Integer numOfPages = function.numOfpages(total , size);
        List<Integer> pages = function.page_list(numOfPages);
        Integer numOfShow = function.numOfShow(total , size , page , numOfPages);

        model.addAttribute("map" , map);
        model.addAttribute("pages" , pages);
        model.addAttribute("numOfShow" , numOfShow);
        model.addAttribute("numOfMentors" , total);
        model.addAttribute("pageIndex" , page + 1);
        model.addAttribute("orderby" , orderby);
        model.addAttribute("search" , search);
        model.addAttribute("link" , "mentor manage");
    }

    @Override
    public void addMentorView(Model model) {
        Function function = new Function();
        function.role(model , userService);
        model.addAttribute("user" , new Users());
        model.addAttribute("positions" , positionRepository.findAllByDel(false));
        model.addAttribute("link" , "mentor manage");

    }

    @Transactional
    @Override
    public void addMentor(Model model, Users users, String position) {
        List<Position> positions = new ArrayList<>();
        positions.add(positionRepository.findByName(position));
        users.setPositions(positions);
        users.setRole("ROLE_MENTOR");
        users.setDel(false);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        Users us = userService.save(users);

        Mentor mentor = new Mentor();
        mentor.setMaxInternship(20);
        mentor.setUserid(us.getId());
        mentor.setDel(false);
        mentorRepository.save(mentor);
    }

    @Override
    public void updateMentorView(Model model, Long id) {
        Function function = new Function();
        function.role(model , userService);
        model.addAttribute("user" , userService.findById(id));
        model.addAttribute("positions" , positionRepository.findAllByDel(false));
        model.addAttribute("link" , "mentor manage");

    }

    @Transactional
    @Override
    public void updateMentor(Model model, Long id, Users users, String position) {
        Users currentUs = userService.findById(id);
        currentUs.setFullname(users.getFullname());
        currentUs.setEmail(users.getEmail());
        currentUs.setPhonenumber(users.getPhonenumber());
        List<Position> positions = new ArrayList<>();
        positions.add(positionRepository.findByName(position));
        currentUs.setPositions(positions);
        userService.save(currentUs);
    }

    @Transactional
    @Override
    public void deleteMentor(Long id) {
        Users users = userService.findById(id);
        users.setDel(true);
        userService.save(users);
        Mentor mentor = mentorRepository.findByUserid(id);
        mentor.setDel(true);
        mentorRepository.save(mentor);
        List<Internship> internships = internshipRepository.findAllByStatusAndMentorsContains(1, mentor);
        for(Internship intern : internships){
            List<Mentor> mentors = intern.getMentors();
            for(Mentor mt : mentors){
                if(mt.getUserid() == mentor.getUserid()){
                    mentors.remove(mt);
                    break;
                }
            }
            internshipService.save(intern);
        }
    }

    @Override
    public void profileMentor(Model model, Long id) {
        Users users = userService.findById(id);
        Mentor mentor = mentorRepository.findByUserid(id);
        List<Internship> internships = mentor.getInternships();
        List<Users> mentor_internship = new ArrayList<>();
        for(Internship it : internships){
            mentor_internship.add(userService.findById(it.getUserid()));
        }
        Function function = new Function();
        function.role(model , userService);
        model.addAttribute("mentor" , users);
        model.addAttribute("mentor_internship" , mentor_internship);
        model.addAttribute("link" , "mentor manage");

    }
}
