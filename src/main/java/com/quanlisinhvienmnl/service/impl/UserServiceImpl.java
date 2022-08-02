package com.quanlisinhvienmnl.service.impl;

import com.quanlisinhvienmnl.entity.Users;
import com.quanlisinhvienmnl.repository.UserRepository;
import com.quanlisinhvienmnl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Users findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public Users save(Users user) {
        return userRepository.save(user);
    }

    @Override
    public List<Users> findByRole(String role) {
        return userRepository.findAllByRole(role);
    }

    @Override
    public List<Users> findByRoleAndDelFlg(String role, boolean del) {
        return userRepository.findAllByRoleAndDel(role , del);
    }

    @Override
    public Users findByUsername(String username) {
        return  userRepository.findByUsername(username);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Users findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
