
package com.iguroo.task.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iguroo.task.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRolename(String rolename); // Fixed method name
}
