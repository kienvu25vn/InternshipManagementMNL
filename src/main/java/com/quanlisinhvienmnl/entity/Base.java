package com.quanlisinhvienmnl.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
public abstract class Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name  = "created_at")
    @CreatedDate
    private Date created_at;

    @Column(name = "created_by")
    @CreatedBy
    private String created_by;

    @Column(name = "modified_at")
    @LastModifiedDate
    private Date modified_at;

    @Column(name = "modified_by")
    @LastModifiedBy
    private String modified_by;

}
