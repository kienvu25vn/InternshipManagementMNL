package com.quanlisinhvienmnl.repository;

import com.quanlisinhvienmnl.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

    Page<Users> findAllByRoleAndDelAndAndFullnameContainsIgnoreCase(String role , boolean del , String fullname , Pageable pageable);

    Page<Users> findAllByRoleAndDel(String role , boolean del , Pageable pageable);
    List<Users> findAllByRoleAndDelAndAndFullnameContainsIgnoreCase(String role , boolean del , String fullname);

    List<Users> findAllByRole(String role);

    List<Users> findAllByRoleAndDel(String role , boolean del);

    List<Users> findAllByDel(boolean delFlg);
}
