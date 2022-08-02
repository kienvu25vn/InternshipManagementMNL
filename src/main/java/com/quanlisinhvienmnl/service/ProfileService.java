package com.quanlisinhvienmnl.service;

import com.quanlisinhvienmnl.entity.Users;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {

    void Profile(Model model);

    void profileEditView(Long id , Model model);

    void profileEdit(Model model , Long id , Users user , MultipartFile multipartFile) throws IOException;
}
