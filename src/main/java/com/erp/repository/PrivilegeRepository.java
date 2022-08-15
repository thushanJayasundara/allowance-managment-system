package com.erp.repository;

import com.erp.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege,Long> {

    Privilege getPrivilegeByPrivilegeName(String privilegeName);

    Boolean existsByPrivilegeName(String privilegeName);
}
