package com.quanlisinhvienmnl.controller;


import com.quanlisinhvienmnl.entity.Users;
import com.quanlisinhvienmnl.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;


@Controller
public class ProfileController {


    @Autowired
    private ProfileService profileService;

    @GetMapping("/profile")
    public String profileDetails(Model model){
        profileService.Profile(model);
        return "profile";
    }
    @GetMapping("/profile/edit")
    public String editProfile(@RequestParam("id") Long id , Model model){
        profileService.profileEditView(id , model);
        return "editprofile";
    }

    @PostMapping("/profile")
    public String editProfileIntern(Model model , @RequestParam("id") Long id , @Valid @ModelAttribute("user") Users user , BindingResult bindingResult, @RequestParam("image") MultipartFile multipartFile ) throws IOException {
        if(bindingResult.hasErrors()){
            profileService.profileEditView(id , model);
            return "editprofile";
        }
        profileService.profileEdit(model , id , user , multipartFile);
        return "redirect:/profile";
    }
}
