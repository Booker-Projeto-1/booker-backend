package com.ufcg.booker.model;

import com.ufcg.booker.dto.UpdateUserRequest;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;

    @Deprecated
    protected User(){}

    public User(String email, String firstName, String lastName, String phoneNumber, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public Long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void updateInformation(UpdateUserRequest updateUserRequest) {
        this.phoneNumber = updateUserRequest.phoneNumber();
        this.firstName = updateUserRequest.firstName();
        this.lastName = updateUserRequest.lastName();
    }
    public String getFirstName() { return  firstName;}

    public String getLastName() {return lastName;}
}
