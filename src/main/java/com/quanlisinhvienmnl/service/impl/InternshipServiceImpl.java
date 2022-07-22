package com.quanlisinhvienmnl.service.impl;

import com.quanlisinhvienmnl.entity.Internship;
import com.quanlisinhvienmnl.entity.Users;
import com.quanlisinhvienmnl.repository.InternshipRepository;
import com.quanlisinhvienmnl.repository.UserRepository;
import com.quanlisinhvienmnl.service.InternshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InternshipServiceImpl implements InternshipService {

    @Autowired
    private InternshipRepository internshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public  List<Internship> findAll() {
        List<Users> users = userRepository.findAllByRole("ROLE_INTERNSHIP");
        List<Internship> internships = new ArrayList<>();
        for(Users user : users){
            internships.add(internshipRepository.findByUserid(user.getId()));
        }
        return internships;
    }

    @Override
    public Internship findByUserId(Long id) {
        return internshipRepository.findByUserid(id);
    }

    @Override
    public void save(Internship internship) {
        internshipRepository.save(internship);
    }

    @Override
    public void deleteByUserId(Long id) {
        Internship internship = internshipRepository.findByUserid(id);
        internship.setStatus(0);
        Users users = userRepository.findById(id).get();
        users.setDel(true);
        userRepository.save(users);
        internshipRepository.save(internship);
    }




}
