package com.batch.config;

import com.batch.entity.Person;
import com.batch.pojo.PersonExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * @author waylon on 05/05/2017.
 */
public class FileDatabaseItemProcessor implements ItemProcessor<Person, PersonExport> {

    private static final Logger LOG = LoggerFactory.getLogger(FileDatabaseItemProcessor.class);

    @Override
    public PersonExport process(Person person) throws Exception {

        PersonExport personExport = new PersonExport();
        personExport.setId(person.getId());
        personExport.setIdCard(person.getIdCard());
        personExport.setName(person.getName());
        personExport.setSurname(person.getSurname());
        personExport.setAge(person.getAge());

        return personExport;
    }
}
