package com.assignment.kirana.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.assignment.kirana.model.User;
import com.assignment.kirana.model.UserDTO;

public interface DefaultUserService extends UserDetailsService{
    User save(UserDTO userRegisteredDTO);
}
