package org.example.boardback.repository.user;

import org.example.boardback.entity.User.Role;
import org.example.boardback.entity.User.User;
import org.example.boardback.entity.User.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
