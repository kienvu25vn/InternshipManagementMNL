package com.quanlisinhvienmnl.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "mentor")
public class Mentor extends Base{


    @Column(name = "user_id")
    private Long userid;

    @Column(name = "max_internship")
    private Integer maxInternship;

    @Column(name = "is_del_flg")
    private boolean del;

    @ManyToMany(mappedBy = "mentors")
    private List<Internship> internships = new ArrayList<>();


    @Override
    public String toString() {
        return "Mentor{" +
                "userid=" + userid +
                ", maxInternship=" + maxInternship +
                ", del=" + del +
                ", internships=" + internships +
                '}';
    }
}
