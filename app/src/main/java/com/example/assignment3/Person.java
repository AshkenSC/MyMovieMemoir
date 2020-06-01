package com.example.assignment3;

import java.sql.Date;

public class Person {
    private int personId;
    private String firstName;
    private String surname;
    private String gender;
    private String DoB;
    private String address;
    private String stateName;
    private String postcode;

    public Person(int personId, String firstName, String surname, String gender, String DoB, String address, String stateName, String postcode) {
        this.personId = personId;
        this.firstName = firstName;
        this.surname = surname;
        this.gender = gender;
        this.DoB = DoB;
        this.address = address;
        this.stateName = stateName;
        this.postcode = postcode;
    }

//    public Person (int personId, String firstName, String surname, String DoB, String address, String stateName, String postcode) {
//        this.personId = personId;
//        this.firstName = firstName;
//    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDoB() {
        return DoB;
    }

    public void setDoB(String DoB) {
        DoB = DoB;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
