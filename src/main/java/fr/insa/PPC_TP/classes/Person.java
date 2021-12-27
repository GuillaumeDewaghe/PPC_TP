package fr.insa.PPC_TP.classes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Person
{
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;
    private String phone;
    private String city;

    public Person()
    {

    }

    public Person(Long id, String name, String surname, String phone, String city) {
        super();
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.city = city;
    }

    public Person(String name, String surname, String phone, String city) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
