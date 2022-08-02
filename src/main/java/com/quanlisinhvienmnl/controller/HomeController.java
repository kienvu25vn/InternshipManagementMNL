package com.quanlisinhvienmnl.controller;

import com.quanlisinhvienmnl.config.DateUtils;
import com.quanlisinhvienmnl.countfunction.Function;
import com.quanlisinhvienmnl.entity.*;
import com.quanlisinhvienmnl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDate;
import java.util.Date;


@Controller
@SessionAttributes({"link", "day"})
public class HomeController {


    @Autowired
    private UserService userService;
    @GetMapping(value = {"/" , "/login"})
    public String index(){
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model ){
        Function function = new Function();
        function.role(model , userService);
        User userCurrent = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = userService.findByUsername(userCurrent.getUsername());
        model.addAttribute("user" , user);
        model.addAttribute("link" , "home");
        model.addAttribute("day" , LocalDate.now());
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


    @GetMapping("/403")
    public String error(){
        return "profile/403Page";
    }
}
