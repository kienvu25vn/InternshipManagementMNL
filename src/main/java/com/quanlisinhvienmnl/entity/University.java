package com.quanlisinhvienmnl.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "university")
public class University extends Base {

    @Column(name = "name" , nullable = false)
    private String name;

    @Column(name = "is_del_flg" , nullable = false)
    private boolean is_del_flg;

}
