package com.batch.entity;

import javax.persistence.*;

/**
 * @author waylon on 05/05/2017.
 */
@Entity
@Table(schema = "public", name = "person")
public class Person {

    @Id
    @Id
    @GeneratedValue(generator = "person_id_seq")
    @SequenceGenerator(name = "person_id_seq", sequenceName = "public.person_id_seq", allocationSize = 1)
    private Long id;
    private String idCard;
    private String name;
    private String surname;
    private Long age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }
}
