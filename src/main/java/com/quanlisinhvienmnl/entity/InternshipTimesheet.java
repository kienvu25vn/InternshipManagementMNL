package com.quanlisinhvienmnl.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "internship_timesheet")
public class InternshipTimesheet extends Base{

    @Column(name = "working_day")
    private Date working_day;

    @Column(name = "time")
    private String time;

    @Column(name = "is_del_flg")
    private boolean is_del_flg;

    @OneToOne
    @JoinColumn(name = "internship_id")
    private Internship internship;

}
