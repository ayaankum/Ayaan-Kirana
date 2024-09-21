package com.assignment.kirana.controller;

import com.assignment.kirana.config.JwtGeneratorValidator;
import com.assignment.kirana.model.User;
import com.assignment.kirana.model.UserDTO;
import com.assignment.kirana.repository.UserRepository;
import com.assignment.kirana.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtGeneratorValidator jwtGenVal;

    @Autowired
    BCryptPasswordEncoder bcCryptPasswordEncoder;

    @Autowired
    DefaultUserService userService;

    @PostMapping("/registration")
    public ResponseEntity<Object> registerUser(@RequestBody UserDTO userDto) {
        User users =  userService.save(userDto);
        if (users.equals(null))
            return generateRespose("Not able to save user ", HttpStatus.BAD_REQUEST, userDto);
        else
            return generateRespose("User saved successfully : ", HttpStatus.OK, users);
    }


    @GetMapping("/genToken")
    public String generateJwtToken(@RequestBody UserDTO userDto) throws Exception {

        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUserName(), userDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtGenVal.generateToken(authentication);
    }

    public ResponseEntity<Object> generateRespose(String message, HttpStatus st, Object responseobj) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("meaasge", message);
        map.put("Status", st.value());
        map.put("data", responseobj);

        return new ResponseEntity<Object>(map, st);
    }


}
