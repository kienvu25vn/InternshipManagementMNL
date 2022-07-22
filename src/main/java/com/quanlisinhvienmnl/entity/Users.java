package com.quanlisinhvienmnl.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "user")
public class Users extends Base{

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "phone_number")
    private String phonenumber;

    @Column(name = "is_del_flg")
    private boolean del;

    @Column(name = "role")
    private String role;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_position" , joinColumns = @JoinColumn(name = "user_id"),
                                        inverseJoinColumns = @JoinColumn(name = "position_id"))
    private List<Position> positions = new ArrayList<>();



}
