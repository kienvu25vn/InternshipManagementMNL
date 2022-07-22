package com.quanlisinhvienmnl.controller;

import com.quanlisinhvienmnl.entity.*;
import com.quanlisinhvienmnl.repository.*;
import com.quanlisinhvienmnl.service.InternshipService;
import com.quanlisinhvienmnl.service.UserService;
import jdk.jfr.Timespan;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class HomeController {


    @GetMapping(value = {"/" , "/login"})
    public String index(){
        return "login";
    }

    @GetMapping("/home")
    public String home(){

        return "home";
    }


    @GetMapping("/loginError")
    public String loginError(Model model){

        model.addAttribute("alertMess", "danger");
        model.addAttribute("mess", "Wrong username or password!");

        return "login";
    }

    @GetMapping("/logoutSuccessful")
    public String logout(Model model){
        model.addAttribute("alertMess", "success");
        model.addAttribute("mess", "You loged out!");

        return "login";
    }

    @GetMapping("/mentor")
    public String mentorManage(){
        return "internshipManage";
    }

    @GetMapping("/403")
    public String error(){
        return "403Page";
    }
}
