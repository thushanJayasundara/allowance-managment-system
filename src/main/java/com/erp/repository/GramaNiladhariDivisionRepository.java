package com.erp.repository;

import com.erp.entity.GramaNiladhariDivision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GramaNiladhariDivisionRepository extends JpaRepository<GramaNiladhariDivision,Long> {

    Boolean existsByDivisionCode(String divisionCode);
}
