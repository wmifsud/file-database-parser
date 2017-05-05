package com.batch.repository;

import com.batch.pojo.PersonExport;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author waylon on 05/05/2017.
 */
public interface PersonRepository extends JpaRepository<PersonExport, Long> {

}
