package com.quanlisinhvienmnl.controller;


import com.quanlisinhvienmnl.countfunction.Function;
import com.quanlisinhvienmnl.entity.*;
import com.quanlisinhvienmnl.repository.*;
import com.quanlisinhvienmnl.service.InternshipService;
import com.quanlisinhvienmnl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class InternshipController {

    @Autowired
    private InternshipService internshipService;

    @Autowired
    private InternshipRepository internshipRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/internship/new")
    public String addInternship(Model model){
        List<Position> positions = positionRepository.findAll();
        List<University> universities = universityRepository.findAll();
        model.addAttribute("positions" , positions);
        model.addAttribute("universities" , universities);
        model.addAttribute("userinternship" , new UserInternship());
        return "addInternship";
    }

    @PostMapping("/internship")
    public String addNewInternship(@RequestParam("uni") String uni , @RequestParam("position") String position , @RequestParam("bd") String bd, @Valid @ModelAttribute("userinternship") UserInternship userInternship ,BindingResult bindingResult

                                  ) throws ParseException {

        if(bindingResult.hasErrors()) {
            return "addInternship";
        }
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
        internship.setUniversity(universityRepository.findByName(uni));
        internship.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(bd));
        internship.setStatus(1);
        internship.setLevel(userInternship.getLevel());
        internship.setIndentify(userInternship.getIndentify());
        internshipService.save(internship);


        return "redirect:/internship";
    }

    @DeleteMapping("/internship")
    public String deleteIntership(@RequestParam("id") String userid){
        internshipService.deleteByUserId(Long.parseLong(userid));
        return "redirect:/internship";
    }


    @GetMapping("/update")
    public String update(@RequestParam("id") String id , Model model){
        Users user = userService.findById(Long.parseLong(id));
        Internship internship = internshipService.findByUserId(Long.parseLong(id));
        UserInternship userInternship = new UserInternship();
        userInternship.setUsername(user.getUsername());
        userInternship.setFullname(user.getFullname());
        userInternship.setPassword(user.getPassword());
        userInternship.setEmail(user.getEmail());
        userInternship.setPhonenumber(user.getPhonenumber());
        userInternship.setIndentify(internship.getIndentify());
        userInternship.setLevel(internship.getLevel());
        List<Position> positions = positionRepository.findAll();
        List<University> universities = universityRepository.findAll();
        model.addAttribute("positions" , positions);
        model.addAttribute("universities" , universities);
        model.addAttribute("userinternship" ,userInternship);
        model.addAttribute("id" , user.getId());
        return "updateInternship";
    }

    @PutMapping("/internship")
    public String updateInternship(@RequestParam("id") String id ,
                                   @RequestParam("uni") String uni ,
                                   @RequestParam("position") String position ,
                                   @RequestParam("bd") String bd,
                                   @Valid @ModelAttribute("userinternship") UserInternship userInternship ,
                                   BindingResult bindingResult) throws ParseException{
        if(bindingResult.hasErrors()){
            return "updateInternship";
        }
        List<Position> positions = new ArrayList<>();
        positions.add(positionRepository.findByName(position));
        Users user = (Users) userService.findById(Long.parseLong(id));
        user.setUsername(userInternship.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(userInternship.getPassword()));
        user.setFullname(userInternship.getFullname());
        user.setEmail(userInternship.getEmail());
        user.setPhonenumber(userInternship.getPhonenumber());
        user.setPositions(positions);
        Users us = userService.save(user);

        Internship internship = internshipService.findByUserId(us.getId());
        internship.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(bd));
        internship.setIndentify(userInternship.getIndentify());
        internship.setLevel(userInternship.getLevel());
        internship.setUniversity(universityRepository.findByName(uni));
        internshipService.save(internship);


        return "redirect:/internship";

    }

    @GetMapping("/internship/profile")
    public String profile(@Param("id") Long id , Model model){
        User us = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<GrantedAuthority> roles = us.getAuthorities();
        String role = "";
        for(GrantedAuthority grantedAuthority : roles){
            if(grantedAuthority.toString().equals("ROLE_ADMIN"))
            {
                role = "ADMIN";
                break;
            }
        }
        model.addAttribute("role" , role);

        Users user = userService.findById(id);
        Internship internship = internshipService.findByUserId(id);

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
        return "internshipProfile";
    }

    @PostMapping("/internship/assignmentor")
    public String assigninternship(@Param("userid") Long userid , @RequestParam("mentor") Long mentorId){
        Internship internship = internshipService.findByUserId(userid);
        List<Mentor> mentors = new ArrayList<>();
        Mentor mentor = mentorRepository.findByUserid(mentorId);
        mentors.add(mentor);
        internship.setMentors(mentors);
        internshipService.save(internship);
        mentor.setMaxInternship(mentor.getMaxInternship() - 1);
        mentorRepository.save(mentor);
        return "redirect:/internship/profile?id=" + userid;
    }

    @PostMapping("/internship/review")
    public String reviewInternship(@Param("userid") Long userid , @Param("reviewer") String reviewer , @RequestParam("content") String content){

        Reviews review = new Reviews();
        review.setReviewer(userService.findByUsername(reviewer));
        review.setInternship(internshipService.findByUserId(userid));
        review.setContent(content);
        reviewRepository.save(review);
        return "redirect:/internship/profile?id=" + userid;
    }

    @GetMapping("/internship")
    public String searchInternship(@RequestParam(value = "search" , required = false , defaultValue = "") String search,
                                   Model model,
                                   @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                   @RequestParam(name = "size", required = false, defaultValue = "2") Integer size,
                                   @RequestParam(name = "orderby" , required = false, defaultValue = "id") String orderby,
                                   @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort){
        if(search == null)
            search = "";
        Function function = new Function();
        Map<Internship, Users> map = new HashMap<>();
        int total = 0;
        Pageable pageable = function.pageable(page , size , orderby , sort);
        if(function.check_role("ROLE_ADMIN")) {
            Page<Users> users = userRepository.findAllByRoleAndDelAndAndFullnameContainsIgnoreCase("ROLE_INTERNSHIP" , false , search , pageable);
            List<Internship> internships = new ArrayList<>();
            total = (int) users.getTotalElements();
            for(Users us : users){
                internships.add(internshipService.findByUserId(us.getId()));
            }
            for(int i = 0; i < internships.size() ; i++){
                map.put(internships.get(i) , users.getContent().get(i));
            }
            model.addAttribute("role", "ADMIN");
        }
        if(function.check_role("ROLE_MENTOR")){
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Users> users = new ArrayList<>();
            Mentor mentor = new Mentor();
            mentor = mentorRepository.findByUserid(userService.findByUsername(user.getUsername()).getId());
            if(search != "" && search != null) {
                users = userRepository.findAllByRoleAndDelAndAndFullnameContainsIgnoreCase("ROLE_INTERNSHIP", false, search);
            }
            else{
                users = userRepository.findAllByRoleAndDel("ROLE_INTERNSHIP" , false);
            }
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
        return "internshipManage";
    }
}
