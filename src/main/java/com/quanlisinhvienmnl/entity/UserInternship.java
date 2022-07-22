package com.quanlisinhvienmnl.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.util.Date;

@Data
public class UserInternship {
    @Size(min = 6 , message = "username must be greater than 5 characters!")
    private String username;

    @Size(min = 8 , message = "password must be greater than 7 characters!")
    private String password;

    @Email
    private String email;

    @NotBlank(message = "please enter the fullname!")
    private String fullname;

    @NotBlank(message = "please enter the phone number!")
    private String phonenumber;

    @NotBlank(message = "please enter the identify card number!")
    private String indentify;

    @Min(value = 0 , message = "level >= 0")
    @NotNull(message = "please enter the level!")
    private Long level;
}
