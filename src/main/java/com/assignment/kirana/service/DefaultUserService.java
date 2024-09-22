package com.assignment.kirana.service;

import com.assignment.kirana.model.User;
import com.assignment.kirana.model.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface DefaultUserService extends UserDetailsService {
  User save(UserDTO userRegisteredDTO);
}
