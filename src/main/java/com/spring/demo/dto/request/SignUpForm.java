package com.spring.demo.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class SignUpForm {
    private long id;
    private String username;
    private String password;
    private String email;
    private String name;
    private Set<String> roles;
}
