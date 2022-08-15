package com.erp.repository;

import com.erp.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person,Long> {

    Person getPersonByNic(String nic);

    Boolean existsByNic(String nic);
}
