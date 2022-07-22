package com.quanlisinhvienmnl.service.impl;

import com.quanlisinhvienmnl.entity.Users;
import com.quanlisinhvienmnl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = this.userRepository.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }

        String roleName = user.getRole();
        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        GrantedAuthority authority = new SimpleGrantedAuthority(roleName);
        grantList.add(authority);
        UserDetails userDetails = (UserDetails) new User(user.getUsername(),
                user.getPassword(), grantList);
        return userDetails;
    }
}
