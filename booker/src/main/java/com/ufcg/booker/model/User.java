package com.ufcg.booker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String email;
    private String fullName;
    private String phoneNumber;
    private String password;

    @Deprecated
    protected User(){}

    public User(String email, String fullName, String phoneNumber, String password) {
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public Long getId() {
        return this.id;
    }
}
