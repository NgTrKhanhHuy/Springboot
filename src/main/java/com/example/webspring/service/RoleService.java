package com.example.webspring.service;
import com.example.webspring.entity.*;
import com.example.webspring.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }
}