package com.volunnear.services.users;

import com.volunnear.entitiy.users.Role;
import com.volunnear.repositories.users.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public List<Role> getRoleByName(String roleName) {
        return List.of(roleRepository.findRoleByName(roleName));
    }
}
