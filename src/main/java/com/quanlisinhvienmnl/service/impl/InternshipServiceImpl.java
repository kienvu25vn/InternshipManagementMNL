package com.quanlisinhvienmnl.service.impl;

import com.quanlisinhvienmnl.countfunction.Function;
import com.quanlisinhvienmnl.entity.*;
import com.quanlisinhvienmnl.model.UserInternship;
import com.quanlisinhvienmnl.repository.*;
import com.quanlisinhvienmnl.service.InternshipService;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SessionAttributes("link")
@Service
public class InternshipServiceImpl implements InternshipService {

    @Autowired
    private InternshipRepository internshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public  List<Internship> findAll() {
        List<Users> users = userRepository.findAllByRole("ROLE_INTERNSHIP");
        List<Internship> internships = new ArrayList<>();
        for(Users user : users){
            internships.add(internshipRepository.findByUserid(user.getId()));
        }
        return internships;
    }

    @Override
    public Internship findByUserId(Long id) {
        return internshipRepository.findByUserid(id);
    }

    @Override
    public void save(Internship internship) {
        internshipRepository.save(internship);
    }

    @Override
    public void deleteByUserId(Long id) {
        Internship internship = internshipRepository.findByUserid(id);
        internship.setStatus(0);
        Users users = userRepository.findById(id).get();
        users.setDel(true);
        userRepository.save(users);
        internshipRepository.save(internship);
    }

    @Override
    public Internship findById(Long id) {
        return internshipRepository.findById(id).get();
    }

    @Override
    public void Internship(Model model, String search, String sort, String orderby, Integer page, Integer size) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Function function = new Function();
        function.role(model , userService);
        if(search == null)  search = "";

        int total = 0;
        Map<Internship, Users> map = new HashMap<>();
        Pageable pageable = function.pageable(page , size , orderby , sort);

        if(function.check_role("ROLE_ADMIN")) {
            Page<Users> users = userRepository.findAllByRoleAndDelAndAndFullnameContainsIgnoreCase("ROLE_INTERNSHIP" , false , search , pageable);
            List<Internship> internships = new ArrayList<>();
            total = (int) users.getTotalElements();

            for(Users us : users){
                internships.add(findByUserId(us.getId()));
            }
            for(int i = 0; i < internships.size() ; i++){
                map.put(internships.get(i) , users.getContent().get(i));
            }

        }
        if(function.check_role("ROLE_MENTOR")){
            List<Users> users = new ArrayList<>();
            Mentor mentor = new Mentor();

            mentor = mentorRepository.findByUserid(userService.findByUsername(user.getUsername()).getId());
            users = userRepository.findAllByRoleAndDelAndAndFullnameContainsIgnoreCase("ROLE_INTERNSHIP", false, search);


            List<Long> userids = new ArrayList<>();
            for(Users us : users){
                userids.add(us.getId());
            }

            Page<Internship> internships = internshipRepository.findAllByStatusAndMentorsContainsAndUseridIn(1 , mentor , userids , pageable);
            List<Users> users_is_mentor = new ArrayList<>();
            for(Internship intern : internships){
                users_is_mentor.add(userService.findById(intern.getUserid()));
            }
            total = (int) internships.getTotalElements();
            for(int i = 0; i < users_is_mentor.size() ; i++){
                map.put(internships.getContent().get(i) ,users_is_mentor.get(i));
            }
        }


        Integer numOfPages = function.numOfpages(total , size);
        List<Integer> pages = function.page_list(numOfPages);
        Integer numOfShow = function.numOfShow(total , size , page , numOfPages);

        model.addAttribute("map" , map);
        model.addAttribute("pages" , pages);
        model.addAttribute("numOfShow" , numOfShow);
        model.addAttribute("numOfInternships" , total);
        model.addAttribute("pageIndex" , page + 1);
        model.addAttribute("orderby" , orderby);
        model.addAttribute("search" , search);
        model.addAttribute("link" , "internship manage");
    }

    @Transactional
    @Override
    public void addNewInternship(Model model, String university, String position, String birthday, UserInternship userInternship) throws ParseException {
        Users user = new Users();
        Internship internship = new Internship();
        List<Position> positions = new ArrayList<>();
        positions.add(positionRepository.findByName(position));
        user.setDel(false);
        user.setRole("ROLE_INTERNSHIP");
        user.setPositions(positions);
        user.setUsername(userInternship.getUsername());
        user.setFullname(userInternship.getFullname());
        user.setEmail(userInternship.getEmail());
        user.setPhonenumber(userInternship.getPhonenumber());
        user.setPassword(passwordEncoder.encode(userInternship.getPassword()));
        Users us = userService.save(user);

        internship.setUserid(us.getId());
        internship.setUniversity(universityRepository.findByName(university));
        internship.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        internship.setStatus(1);
        internship.setLevel(userInternship.getLevel());
        internship.setIndentify(userInternship.getIndentify());
        save(internship);
    }

    @Override
    public void updateInternshipView(Long id, Model model) {
        Function function = new Function();
        function.role(model , userService);
        Users user = userService.findById(id);
        Internship internship = findByUserId(id);
        UserInternship userInternship = new UserInternship();
        userInternship.setUserid(id);
        userInternship.setFullname(user.getFullname());
        userInternship.setEmail(user.getEmail());
        userInternship.setPhonenumber(user.getPhonenumber());
        userInternship.setIndentify(internship.getIndentify());
        userInternship.setLevel(internship.getLevel());
        List<Position> positions = positionRepository.findAll();
        List<University> universities = universityRepository.findAll();
        model.addAttribute("positions" , positions);
        model.addAttribute("universities" , universities);
        model.addAttribute("userinternship" ,userInternship);
        model.addAttribute("link" , "internship manage");
    }

    @Transactional
    @Override
    public void updateInternship(Model model, Long id, String university, String position, String birthday, UserInternship userInternship) throws ParseException {
        List<Position> positions = new ArrayList<>();
        positions.add(positionRepository.findByName(position));
        Users user =  userService.findById(id);
        user.setFullname(userInternship.getFullname());
        user.setEmail(userInternship.getEmail());
        user.setPhonenumber(userInternship.getPhonenumber());
        user.setPositions(positions);
        Users us = userService.save(user);

        Internship internship = findByUserId(us.getId());
        internship.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        internship.setIndentify(userInternship.getIndentify());
        internship.setLevel(userInternship.getLevel());
        internship.setUniversity(universityRepository.findByName(university));
        save(internship);
    }

    @Override
    public void internshipProfile(Long id, Model model) {
        Function function = new Function();
        function.role(model ,userService);

        Users user = userService.findById(id);
        Internship internship = findByUserId(id);

        List<Mentor> mentor_internship = internship.getMentors();
        List<Users> mentor_internships = new ArrayList<>();
        for(int i = 0 ;i<mentor_internship.size() ; i++){
            mentor_internships.add(userService.findById(mentor_internship.get(i).getUserid()));
        }
        if(mentor_internships.size() == 0)
            mentor_internships = null;

        List<Users> mentors = new ArrayList<>();
        List<Mentor> mentor_detail =mentorRepository.findAllByMaxInternshipGreaterThanAndDel(0 ,false);
        for(Mentor mentor : mentor_detail){
            mentors.add(userService.findById(mentor.getUserid()));
        }

        List<Reviews> reviews = reviewRepository.findAllByInternship(internship);


        model.addAttribute("user" , user);
        model.addAttribute("internship" , internship);
        model.addAttribute("mentors" , mentors);
        model.addAttribute("mentor_internship" , mentor_internships);
        model.addAttribute("reviews" , reviews);
        model.addAttribute("totalReviews" , reviews.size());
        model.addAttribute("link" , "internship manage");
    }

    @Transactional
    @Override
    public void internshipAssignment(Long userId, Long mentorId) {
        Internship internship = findByUserId(userId);
        List<Mentor> mentors = new ArrayList<>();
        Mentor mentor = mentorRepository.findByUserid(mentorId);
        mentors.add(mentor);
        internship.setMentors(mentors);
        save(internship);
        mentor.setMaxInternship(mentor.getMaxInternship() - 1);
        mentorRepository.save(mentor);
    }

    @Override
    public void internshipReview(Long id, String reviewer, String content) {
        Reviews review = new Reviews();
        review.setReviewer(userService.findByUsername(reviewer));
        review.setInternship(findByUserId(id));
        review.setContent(content);
        reviewRepository.save(review);
    }

    @Override
    public void internshipError(Model model) {
        Function function = new Function();
        function.role(model , userService);
        List<Position> positions = positionRepository.findAllByDel(false);
        List<University> universities = universityRepository.findAll();
        model.addAttribute("positions" , positions);
        model.addAttribute("universities" , universities);
        model.addAttribute("link" , "internship manage");
    }


}
