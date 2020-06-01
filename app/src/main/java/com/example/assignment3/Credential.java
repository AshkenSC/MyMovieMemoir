package com.example.assignment3;

import com.example.assignment3.Person;

import java.sql.Date;

public class Credential {
    private int credentialId;
    private String username;
    private String passwordHash;
    private String signupDate;

    public Credential(int credentialId, String username, String passwordHash, String signupDate, Person personId) {
        this.credentialId = credentialId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.signupDate = signupDate;
        this.personId = personId;
    }

    private Person personId;

    public int getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(int credentialId) {
        this.credentialId = credentialId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(String signupDate) {
        this.signupDate = signupDate;
    }

    public Person getPersonId() {
        return personId;
    }

    public void setPersonId(Person personId) {
        this.personId = personId;
    }
}
