package com.quanlisinhvienmnl.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "internship_timesheet")
public class InternshipTimesheet extends Base{

    @Column(name = "working_day")
    private Date workingDay;

    @Column(name = "time")
    private String time;

    @Column(name = "is_del_flg")
    private boolean del;

    @Column(name =  "total_hours")
    private Integer totalHours;

    @OneToOne
    @JoinColumn(name = "internship_id")
    private Internship internship;

    public InternshipTimesheet(){

    }
    public InternshipTimesheet(Date workingDay , Integer totalHours){
        this.workingDay = workingDay;
        this.totalHours = totalHours;
    }

    @Override
    public String toString() {
        return "InternshipTimesheet{" +
                "workingDay=" + workingDay +
                ", time='" + time + '\'' +
                ", del=" + del +
                ", totalHours=" + totalHours +
                ", internship=" + internship +
                '}';
    }
}
