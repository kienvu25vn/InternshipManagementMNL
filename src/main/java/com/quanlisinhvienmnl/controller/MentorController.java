package com.quanlisinhvienmnl.controller;


import com.quanlisinhvienmnl.entity.Users;
import com.quanlisinhvienmnl.repository.*;

import com.quanlisinhvienmnl.service.MentorService;
import com.quanlisinhvienmnl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
public class MentorController {

    @Autowired
    private MentorService mentorService;

    @Autowired
    private UserService userService;


    @Autowired
    private PositionRepository positionRepository;



    @GetMapping("/mentor")
    public String mentorManagement(@RequestParam(value = "search" , required = false , defaultValue = "") String search,
                                   Model model,
                                   @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                   @RequestParam(name = "size", required = false, defaultValue = "2") Integer size,
                                   @RequestParam(name = "orderby" , required = false, defaultValue = "id") String orderby,
                                   @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort){
        mentorService.Mentor(model , search , sort ,orderby , page , size);
        return "mentorManage";
    }

    @GetMapping("/mentor/new")
    public String addMentorView(Model model){
        mentorService.addMentorView(model);
        return "addMentor";
    }

    @PostMapping("/mentor")
    public String addMentor(Model model ,@Valid @ModelAttribute("user") Users users , BindingResult bindingResult , @RequestParam("position")String position){
        if(bindingResult.hasErrors()){
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("photo", userService.findByUsername(user.getUsername()).getPhotosImagePath());
            model.addAttribute("positions" , positionRepository.findAllByDel(false));
            return "addMentor";
        }
        mentorService.addMentor(model , users , position);
        return "redirect:/mentor";
    }

    @GetMapping("/mentor/update")
    public String updateMentorView(@RequestParam("id") Long id , Model model){
        mentorService.updateMentorView(model , id);
        return "editMentor";
    }

    @PutMapping("/mentor")
    public String updateMentor(@RequestParam("id") Long id,@RequestParam("position") String position ,@Valid @ModelAttribute("user") Users users , BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("photo", userService.findByUsername(user.getUsername()).getPhotosImagePath());
            model.addAttribute("positions" , positionRepository.findAllByDel(false));
            return "editMentor";
        }
        mentorService.updateMentor(model , id , users , position);
        return "redirect:/mentor";
    }

    @DeleteMapping("/mentor")
    public String deleteMentor(@RequestParam("id") Long id ){
        mentorService.deleteMentor(id);
        return "redirect:/mentor";
    }
    @GetMapping("mentor/profile")
    public String mentorProfile(@RequestParam("id") Long id , Model model){
        mentorService.profileMentor(model ,id);
        return "mentorProfile";
    }
}
