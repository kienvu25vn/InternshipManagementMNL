package com.quanlisinhvienmnl.entity;

import com.quanlisinhvienmnl.validation.EmailExisted;
import com.quanlisinhvienmnl.validation.UsernameExisted;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.util.Date;

@Data
public class UserInternship {
    private Long userid;
    @Size(min = 6 , message = "{message.username}")
    private String username;

    @Size(min = 8 , message = "{message.password}")
    private String password;

    @Email(message = "{message.email}")
    private String email;

    @NotBlank(message = "{message.fullname}")
    private String fullname;

    @NotBlank(message = "{message.phone_number}")
    private String phonenumber;

    @NotBlank(message = "{message.indentify}")
    private String indentify;

    @Min(value = 0 , message = "{message.level.min}")
    @NotNull(message = "{message.level.null}")
    private Long level;


    public UserInternship(){

    }
    public UserInternship(Users users , Internship internship){
        this.userid = users.getId();
        this.username = users.getUsername();
        this.password = users.getPassword();
        this.fullname = users.getFullname();
        this.email = users.getEmail();
        this.phonenumber = users.getPhonenumber();
        this.indentify = internship.getIndentify();
        this.level = internship.getLevel();
    }
}
