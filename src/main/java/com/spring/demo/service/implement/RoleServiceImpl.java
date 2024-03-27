package com.spring.demo.service.implement;

import com.spring.demo.model.Role;
import com.spring.demo.model.RoleName;
import com.spring.demo.repository.IRoleRepository;
import com.spring.demo.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private IRoleRepository roleRepository;
    @Override
    public Optional<Role> findByRole(RoleName role) {
        return roleRepository.findByRole(role);
    }
}
