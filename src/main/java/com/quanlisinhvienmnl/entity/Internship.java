package com.quanlisinhvienmnl.entity;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "intership")
public class Internship extends Base{

    @Column(name = "user_id")
    private Long userid;

    @Column(name = "birthday")
    @DateTimeFormat(pattern = "dd/MM/yyy")
    private Date birthday;

    @Column(name = "scholastic")
    private Long scholastic;

    @Column(name = "indentify_card")
    private String indentify;

    @Column(name = "level")
    private Long level;

    @Column(name = "status")
    private Integer status;

    @OneToOne
    @JoinColumn(name = "company_card_id")
    private CompanyCard companyCard;

    @OneToOne
    @JoinColumn(name = "university_id")
    private University university;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "internship_mentor", joinColumns = @JoinColumn(name = "internship_id"),
            inverseJoinColumns = @JoinColumn(name = "mentor_id"))
    private List<Mentor> mentors = new ArrayList<>();


}
