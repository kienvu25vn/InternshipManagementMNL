package com.quanlisinhvienmnl.service.impl;

import com.quanlisinhvienmnl.countfunction.Function;
import com.quanlisinhvienmnl.entity.Internship;
import com.quanlisinhvienmnl.entity.Mentor;
import com.quanlisinhvienmnl.entity.Reviews;
import com.quanlisinhvienmnl.entity.Users;
import com.quanlisinhvienmnl.repository.ReviewRepository;
import com.quanlisinhvienmnl.service.InternshipService;
import com.quanlisinhvienmnl.service.ProfileService;
import com.quanlisinhvienmnl.service.UserService;
import com.quanlisinhvienmnl.uploadFile.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private UserService userService;

    @Autowired
    private InternshipService internshipService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public void Profile(Model model) {
        Function function = new Function();
        function.role(model ,userService);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userService.findByUsername(user.getUsername());

        if(users.getRole().equals("ROLE_INTERNSHIP")){
            Internship internship = internshipService.findByUserId(users.getId());
            List<Reviews> reviews = reviewRepository.findAllByInternship(internship);
            List<Mentor> mentors = internship.getMentors();
            List<Users> mentor_internship = new ArrayList<>();
            for(int i = 0 ;i<mentors.size() ; i++){
                mentor_internship.add(userService.findById(mentors.get(i).getUserid()));
            }
            model.addAttribute("user" , users);
            model.addAttribute("internship" , internship);
            model.addAttribute("mentor_internship" , mentor_internship);
            model.addAttribute("reviews" , reviews);
            model.addAttribute("totalReviews" , reviews.size());
        }
        else{
            model.addAttribute("user" , users);
        }
    }

    @Override
    public void profileEditView(Long id, Model model) {
        Function function = new Function();
        function.role(model ,userService);
        Users users = userService.findById(id);
        model.addAttribute("user" , users);
    }

    @Override
    public void profileEdit(Model model, Long id, Users user, MultipartFile multipartFile) throws IOException {
        Users currentUser = userService.findById(id);
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if(!fileName.equals("")) {
            currentUser.setPhotos(fileName);
            String uploadDir = "user-photos/" + id;
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        currentUser.setFullname(user.getFullname());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhonenumber(user.getPhonenumber());
        Users saveUser = userService.save(currentUser);
    }
}
