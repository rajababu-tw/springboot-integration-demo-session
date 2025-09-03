package com.ford.springboot_integration_demo_session.repository;

import com.ford.springboot_integration_demo_session.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users,Long> {
}
