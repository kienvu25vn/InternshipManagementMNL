package com.quanlisinhvienmnl.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "company_card")
@EntityListeners(AuditingEntityListener.class)
public class CompanyCard {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "using_flg" , nullable = false)
    private boolean using_flg;

    @Column(name = "is_del_flg" , nullable = false)
    private boolean is_del_flg;

    @Column(name  = "created_at")
    @CreatedDate
    private Date created_at;

    @Column(name = "created_by")
    @CreatedBy
    private String created_by;

    @Column(name = "modified_at")
    private Date modified_at;

    @Column(name = "modified_by")
    @LastModifiedBy
    private String modified_by;
}
