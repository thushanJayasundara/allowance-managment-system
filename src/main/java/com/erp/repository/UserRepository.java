package com.erp.repository;

import com.erp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User getUserByToken(String token);

    User getUserByUserName(String userName);

    Boolean existsByUserName(String userName);
}
