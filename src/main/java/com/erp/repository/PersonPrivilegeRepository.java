package com.erp.repository;

import com.erp.entity.PersonPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonPrivilegeRepository extends JpaRepository<PersonPrivilege, Long> {

    PersonPrivilege findByPersonId(long personId);

}
