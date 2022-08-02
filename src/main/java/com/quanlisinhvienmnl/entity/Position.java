package com.quanlisinhvienmnl.entity;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "position")
public class Position extends Base{

    @Column(name = "name" , nullable = false)
    private String name;

    @Column(name = "is_del_flg" , nullable = false)
    private boolean del;

    @ManyToMany(mappedBy = "positions")
    private List<Users> users = new ArrayList<>();
}
