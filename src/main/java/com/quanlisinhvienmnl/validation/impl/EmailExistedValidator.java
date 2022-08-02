package com.quanlisinhvienmnl.validation.impl;

import com.quanlisinhvienmnl.entity.Users;
import com.quanlisinhvienmnl.service.UserService;
import com.quanlisinhvienmnl.validation.EmailExisted;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailExistedValidator implements ConstraintValidator<EmailExisted , String> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Users users = userService.findByEmail(value);
        if(users != null)
            return false;
        return true;
    }
}
