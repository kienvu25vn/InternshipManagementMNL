package com.quanlisinhvienmnl.countfunction;

import com.quanlisinhvienmnl.entity.Users;
import com.quanlisinhvienmnl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Function {



    public  Integer numOfpages(int total , int size){
        int numOfPages = 0;
        if(total % size == 0)
            numOfPages = total / size;
        else
            numOfPages = total / size + 1;
        return numOfPages;
    }

    public  List<Integer> page_list(int numOfPages){
        List<Integer> pages = new ArrayList<>();
        for(int i= numOfPages ; i>=1 ;i--)pages.add(i);
        return pages;
    }

    public  Integer numOfShow(int total , int size , int page , int numOfPages){
        int numOfShow = 0;
        if(page + 1 < numOfPages)
            numOfShow = size;
        if(page + 1 == numOfPages)
            numOfShow = total - size * (page);
        return numOfShow;
    }

    public Pageable pageable(int page , int size , String orderby , String sort){
        Sort sortable = null;
        if (sort.equals("ASC")) {
            sortable = Sort.by(orderby).ascending();
        }
        if (sort.equals("DESC")) {
            sortable = Sort.by(orderby).descending();
        }
        Pageable pageable = PageRequest.of(page, size, sortable);
        return pageable;
    }

    public static boolean check_role(String role){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<GrantedAuthority> roles = user.getAuthorities();
        for(GrantedAuthority grantedAuthority : roles){
            if(grantedAuthority.toString().equals(role))
            {
                return true;
            }
        }
        return false;
    }
    public static void role(Model model , UserService userService){
        if(check_role("ROLE_ADMIN"))
            model.addAttribute("role" , "ADMIN");
        else if(check_role("ROLE_INTERNSHIP"))
            model.addAttribute("role" , "INTERNSHIP");
        else if (check_role("ROLE_MENTOR")) {
            model.addAttribute("role" , "MENTOR");
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("photo", userService.findByUsername(user.getUsername()).getPhotosImagePath());
    }



}
