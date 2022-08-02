package com.quanlisinhvienmnl.validation.impl;

import com.quanlisinhvienmnl.entity.Users;
import com.quanlisinhvienmnl.service.UserService;
import com.quanlisinhvienmnl.validation.UsernameExisted;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameExistedValidator implements ConstraintValidator<UsernameExisted, String> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value.length() < 8)
            return true;
        Users users = userService.findByUsername(value);
        if(users != null)
            return false;
        return true;
    }
}
