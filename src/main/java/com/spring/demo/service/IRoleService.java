package com.spring.demo.service;

import com.spring.demo.model.Role;
import com.spring.demo.model.RoleName;

import java.util.Optional;

public interface IRoleService {
    Optional<Role> findByRole(RoleName roleName);
}
