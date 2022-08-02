package com.quanlisinhvienmnl.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "reviews")
public class Reviews extends Base{

    @Column(name = "type")
    private Integer type;

    @Column(name = "content")
    private String content;

    @Column(name = "is_del_flg")
    private boolean is_del_flg;

    @OneToOne
    @JoinColumn(name = "review_id")
    private Users reviewer;

    @OneToOne
    @JoinColumn(name = "object_id")
    private Internship internship;

}
