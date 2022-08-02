package com.quanlisinhvienmnl.service;

import com.quanlisinhvienmnl.entity.Users;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface UserService {
    List<Users> findAll();

    Users findById(Long id);

    Users save(Users users);

    List<Users> findByRole(String role);

    List<Users> findByRoleAndDelFlg(String role , boolean del);
    Users findByUsername(String username);
    void deleteById(Long id);

    Users findByEmail(String email);
}
